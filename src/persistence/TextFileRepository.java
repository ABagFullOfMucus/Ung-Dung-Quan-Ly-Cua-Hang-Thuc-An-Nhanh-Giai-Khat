package persistence;

import java.io.IOException;

import model.base.BaseEntity;

public class TextFileRepository<T extends BaseEntity> implements Repository<T> {
    private String filePath;
    private T[] items;
    private int count;
    private TextConverter<T> converter;

    @SuppressWarnings("unchecked")
    public TextFileRepository(String filePath, TextConverter<T> converter) {
        this.filePath = filePath;
        this.converter = converter;
        this.items = (T[]) new BaseEntity[10];
        this.count = 0;
    }

    private void resize() {
        @SuppressWarnings("unchecked")
        T[] newItems = (T[]) new BaseEntity[items.length * 2];
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
        T[] result = (T[]) new BaseEntity[count];
        for (int i = 0; i < count; i++) {
            result[i] = items[i];
        }
        return result;
    }

    @Override
    public T findById(String id) {
        for (int i = 0; i < count; i++) {
            if (items[i].getId().equals(id)) {
                return items[i];
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
