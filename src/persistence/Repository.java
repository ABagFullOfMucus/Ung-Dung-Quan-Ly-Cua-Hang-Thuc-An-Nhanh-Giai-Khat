package persistence;

public interface Repository<T> {

    boolean add(T entity);

    boolean update(T entity);

    T findById(String id);

    T[] findAll();
}
