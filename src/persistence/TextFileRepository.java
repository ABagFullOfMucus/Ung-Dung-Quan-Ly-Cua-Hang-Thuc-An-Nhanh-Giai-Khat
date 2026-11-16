package persistence;

import java.io.IOException;
import java.lang.reflect.Array;

import model.base.BaseEntity;

public class TextFileRepository<T extends BaseEntity> implements Repository<T> {
    private String filePath;
    private T[] items;
    private int count;
    private final TextConverter<T> converter;
    private final Class<T> type;

    @SuppressWarnings("unchecked")
    public TextFileRepository(String filePath, TextConverter<T> converter, Class<T> type) {
        this.filePath = filePath;
        this.converter = converter;
        this.type = type;
        this.items = (T[]) Array.newInstance(type, 10);
        this.count = 0;
    }

    private void resize() {
        T[] newItems = (T[]) Array.newInstance(type, items.length * 2);
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

    public void load() throws IOException {
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
        if (entity == null || findById(entity.getId()) != null) {
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
        T[] result = (T[]) Array.newInstance(type, count);
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
            T entity = items[i];
            if (entity != null && entity.getId() != null && entity.getId().equals(id)) {
                return entity;
            }
        }
        return null;
    }

    @Override
    public boolean update(T entity) {
        if (entity == null) {
            return false;
        }
        for (int i = 0; i < count; i++) {
            if (items[i].getId().equals(entity.getId())) {
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
