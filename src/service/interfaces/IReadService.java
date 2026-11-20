package service.interfaces;

import common.Result;

public interface IReadService<T> {
    Result<T> getById(String id);

    T[] getAll();
}