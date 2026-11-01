package persistence;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import model.base.BaseEntity;

public class TextFileRepository<T extends BaseEntity> implements Repository<T> {
    private String filePath;
    private List<T> items;
    private TextConverter<T> converter;

    public TextFileRepository(String filePath, TextConverter<T> converter) {
        this.filePath = filePath;
        this.converter = converter;
        this.items = new ArrayList<>();
    }

    public boolean flush() {
        List<String> lines = new ArrayList<>();
        for (T entity : items) {
            lines.add(converter.toLine(entity));
        }
        return FileHandler.writeLines(filePath, lines);
    }

    public void load() throws IOException {
        items.clear();
        List<String> lines = FileHandler.readLines(filePath);
        for (String line : lines) {
            if (line == null || line.isBlank()) {
                continue;
            }
            T entity = converter.fromLine(line);
            items.add(entity);
        }
    }

    @Override
    public boolean add(T entity) {
        if (entity == null || findById(entity.getId()) != null) {
            return false;
        }
        items.add(entity);
        if (!flush()) {
            items.remove(entity);
            return false;
        }
        return true;
    }

    @Override
    public List<T> findAll() {
        return new ArrayList<>(items);
    }

    @Override
    public T findById(String id) {
        for (T entity : items) {

            if (entity.getId().equals(id)) {
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
        for (int i = 0; i < items.size(); i++) {
            if (items.get(i).getId().equals(entity.getId())) {
                T old = items.set(i, entity);
                if (!flush()) {
                    items.set(i, old);
                    return false;
                }
                return true;
            }
        }
        return false;
    }

}
