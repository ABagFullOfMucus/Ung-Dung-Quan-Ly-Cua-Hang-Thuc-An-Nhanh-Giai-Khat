package service.base;

import common.Result;
import model.base.BaseEntity;
import persistence.Repository;
import service.interfaces.ICrudService;
import utils.ArrayUtils;
import utils.IdGenerator;
import utils.Validator;

public abstract class CrudService<T extends BaseEntity> implements ICrudService<T> {
    private final Repository<T> repository;

    protected CrudService(Repository<T> repository) {
        if (repository == null) {
            throw new IllegalArgumentException("Repository không được null");
        }
        this.repository = repository;
    }

    protected final Repository<T> getRepository() {
        return repository;
    }

    protected abstract String idPrefix();

    protected abstract String validate(T entity);

    protected abstract T[] newArray(int size);

    protected String checkDeactivatable(T entity) {
        return null;
    }

    protected final String nextId() {
        String[] existingIds = ArrayUtils.extractIds(repository.findAll());
        return IdGenerator.next(idPrefix(), existingIds);
    }

    protected final boolean isSameId(T a, T b) {
        return a != null && b != null && a.getId() != null && a.getId().equals(b.getId());
    }

    @Override
    public Result<T> add(T entity) {
        if (entity == null) {
            return Result.fail("Dữ liệu không hợp lệ");
        }
        if (!entity.isActive()) {
            return Result.fail("Không thể thêm mới bản ghi ở trạng thái đã vô hiệu hóa");
        }

        String originalId = entity.getId();
        entity.setId(null);
        String error = validate(entity);
        if (error != null) {
            entity.setId(originalId);
            return Result.fail(error);
        }

        entity.setId(nextId());
        if (!repository.add(entity)) {
            entity.setId(originalId);
            return Result.fail("Không thể lưu dữ liệu");
        }
        return Result.ok(entity);
    }

    @Override
    public Result<T> update(T entity) {
        if (entity == null || !Validator.isNotBlank(entity.getId())) {
            return Result.fail("Dữ liệu hoặc mã không hợp lệ");
        }

        String originalId = entity.getId();
        String cleanId = originalId.trim();
        T existing = repository.findById(cleanId);
        if (existing == null) {
            return Result.fail("Không tìm thấy dữ liệu có mã " + cleanId);
        }
        if (!existing.isActive()) {
            return Result.fail("Bản ghi đã bị vô hiệu hóa, không thể cập nhật");
        }
        if (!entity.isActive()) {
            return Result.fail("Không thể dùng cập nhật để vô hiệu hóa bản ghi, hãy dùng chức năng khóa/xóa mềm");
        }

        entity.setId(cleanId);
        String error = validate(entity);
        if (error != null) {
            entity.setId(originalId);
            return Result.fail(error);
        }
        if (!repository.update(entity)) {
            entity.setId(originalId);
            return Result.fail("Không thể cập nhật dữ liệu");
        }
        return Result.ok(entity);
    }

    @Override
    public Result<T> deactivate(String id) {
        if (!Validator.isNotBlank(id)) {
            return Result.fail("Mã dữ liệu không hợp lệ");
        }
        String cleanId = id.trim();
        T entity = repository.findById(cleanId);
        if (entity == null) {
            return Result.fail("Không tìm thấy dữ liệu có mã " + cleanId);
        }
        if (!entity.isActive()) {
            return Result.fail("Bản ghi đã bị vô hiệu hóa trước đó");
        }

        String reason = checkDeactivatable(entity);
        if (reason != null) {
            return Result.fail(reason);
        }

        entity.deactivate();
        if (!repository.update(entity)) {
            entity.activate();
            return Result.fail("Không thể lưu trạng thái vô hiệu hóa");
        }
        return Result.ok(entity);
    }

    @Override
    public Result<T> getById(String id) {
        if (!Validator.isNotBlank(id)) {
            return Result.fail("Mã dữ liệu không hợp lệ");
        }
        String cleanId = id.trim();
        T entity = repository.findById(cleanId);
        if (entity == null) {
            return Result.fail("Không tìm thấy dữ liệu có mã " + cleanId);
        }
        return Result.ok(entity);
    }

    @Override
    public T[] getAll() {
        T[] all = repository.findAll();
        return all == null ? newArray(0) : all;
    }

    @Override
    public T[] getActive() {
        T[] all = getAll();
        T[] buffer = newArray(all.length);
        for (int i = 0; i < all.length; i++) {
            if (all[i] != null && all[i].isActive()) {
                buffer[i] = all[i];
            }
        }
        int activeCount = ArrayUtils.countNonNull(buffer);
        return ArrayUtils.compact(buffer, newArray(activeCount));
    }
}