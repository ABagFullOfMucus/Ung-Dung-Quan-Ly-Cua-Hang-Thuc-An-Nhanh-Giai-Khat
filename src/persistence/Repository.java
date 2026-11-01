package persistence;

import java.util.List;

public interface Repository<T> {

    boolean add(T entity);

    boolean update(T entity);

    T findById(String id);

    List<T> findAll();
}
