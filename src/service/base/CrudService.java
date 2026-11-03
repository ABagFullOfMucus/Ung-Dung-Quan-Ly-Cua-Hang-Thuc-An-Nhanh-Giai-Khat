package service.base;


import common.Result;
import model.base.BaseEntity;
import utils.IdGenerator;
import persistence.Repository;

import java.util.Arrays;
import java.util.List;

public abstract class CrudService<T extends BaseEntity> {

    protected Repository<T> repository;

    public CrudService(Repository<T> repository) {
        this.repository = repository;
    }

    protected abstract String newId();

    protected String validate(T entity) {
        return null;
    }

    protected String nextId(String prefix) {
        List<T> all = repository.findAll();
        String[] ids = new String[all.size()];
        for (int i = 0; i < all.size(); i++) {
            ids[i] = all.get(i).getId();
        }
        return IdGenerator.next(prefix, Arrays.asList(ids));
    }

    public Result<T> add(T entity) {
        if (entity == null) {
            return Result.fail("Dữ liệu không hợp lệ");
        }
        String error = validate(entity);
        if (error != null) {
            return Result.fail(error);
        }
        entity.setId(newId());
        if (!repository.add(entity)) {
            return Result.fail("Không thể lưu dữ liệu");
        }
        return Result.ok(entity);
    }

    public Result<T> update(T entity) {
        if (entity == null || entity.getId() == null) {
            return Result.fail("Dữ liệu không hợp lệ");
        }
        if (repository.findById(entity.getId()) == null) {
            return Result.fail("Không tìm thấy dữ liệu có mã " + entity.getId());
        }
        String error = validate(entity);
        if (error != null) {
            return Result.fail(error);
        }
        if (!repository.update(entity)) {
            return Result.fail("Không thể cập nhật dữ liệu");
        }
        return Result.ok(entity);
    }

    public Result<T> deactivate(String id) {
        T entity = repository.findById(id);
        if (entity == null) {
            return Result.fail("Không tìm thấy dữ liệu có mã " + id);
        }
        entity.deactivate();
        if (!repository.update(entity)) {
            return Result.fail("Không thể cập nhật dữ liệu");
        }
        return Result.ok(entity);
    }

    public Result<T> getById(String id) {
        T entity = repository.findById(id);
        if (entity == null) {
            return Result.fail("Không tìm thấy dữ liệu có mã " + id);
        }
        return Result.ok(entity);
    }

    public List<T> getAll() {
        return repository.findAll();
    }
}