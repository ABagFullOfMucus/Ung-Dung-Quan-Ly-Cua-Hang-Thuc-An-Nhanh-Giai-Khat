package persistence;

import java.lang.reflect.Array;
import model.base.BaseEntity;

public class TextFileRepository<T extends BaseEntity> implements Repository<T> {
    private final String filePath;
    private final TextConverter<T> converter;
    private final Class<T> entityType;
    private T[] items;
    private int count;

    @SuppressWarnings("unchecked")
    public TextFileRepository(String filePath, TextConverter<T> converter, Class<T> entityType) {
        if (filePath == null || filePath.isBlank()) {
            throw new IllegalArgumentException("filePath không được rỗng");
        }
        if (converter == null) {
            throw new IllegalArgumentException("converter không được null");
        }
        if (entityType == null) {
            throw new IllegalArgumentException("entityType không được null");
        }
        this.filePath = filePath;
        this.converter = converter;
        this.entityType = entityType;
        this.items = (T[]) Array.newInstance(entityType, 10);
        this.count = 0;
    }

    @SuppressWarnings("unchecked")
    private void resize() {
        T[] newItems = (T[]) Array.newInstance(entityType, items.length * 2);
        for (int i = 0; i < count; i++) {
            newItems[i] = items[i];
        }
        items = newItems;
    }

    public boolean flush() {
        String[] lines = new String[count];
        for (int i = 0; i < count; i++) {
            lines[i] = converter.toLine(items[i]);
        }
        return FileHandler.writeLines(filePath, lines);
    }

    public void load() {
        count = 0;
        String[] lines = FileHandler.readLines(filePath);
        for (String line : lines) {
            if (line == null || line.isBlank()) {
                continue;
            }
            T entity = converter.fromLine(line);
            if (entity == null) {
                continue;
            }
            if (count >= items.length) {
                resize();
            }
            items[count++] = entity;
        }
    }

    @Override
    public boolean add(T entity) {
        if (entity == null || entity.getId() == null || findById(entity.getId()) != null) {
            return false;
        }
        if (count >= items.length) {
            resize();
        }
        items[count] = entity;
        count++;
        if (!flush()) {
            count--;
            items[count] = null;
            return false;
        }
        return true;
    }

    @Override
    @SuppressWarnings("unchecked")
    public T[] findAll() {
        T[] result = (T[]) Array.newInstance(entityType, count);
        for (int i = 0; i < count; i++) {
            result[i] = items[i];
        }
        return result;
    }

    @Override
    public T findById(String id) {
        if (id == null) {
            return null;
        }
        for (int i = 0; i < count; i++) {
            if (items[i] != null && id.equals(items[i].getId())) {
                return items[i];
            }
        }
        return null;
    }

    @Override
    public boolean update(T entity) {
        if (entity == null || entity.getId() == null) {
            return false;
        }
        for (int i = 0; i < count; i++) {
            if (items[i] != null && entity.getId().equals(items[i].getId())) {
                T old = items[i];
                items[i] = entity;
                if (!flush()) {
                    items[i] = old;
                    return false;
                }
                return true;
            }
        }
        return false;
    }
}