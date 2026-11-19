package persistence;

public interface TextConverter<T> {
    String toLine(T entity);

    T fromLine(String line);
}