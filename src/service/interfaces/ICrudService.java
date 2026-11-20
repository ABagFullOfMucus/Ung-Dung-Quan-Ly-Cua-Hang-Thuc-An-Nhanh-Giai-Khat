package service.interfaces;

import common.Result;

public interface ICrudService<T> extends IReadService<T> {
    Result<T> add(T entity);

    Result<T> update(T entity);

    Result<T> deactivate(String id);

    T[] getActive();
}