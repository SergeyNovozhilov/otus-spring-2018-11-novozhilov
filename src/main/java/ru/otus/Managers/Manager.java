package ru.otus.Managers;

import ru.otus.Domain.Base;
import ru.otus.Exceptions.DBException;
import ru.otus.Exceptions.NotFoundException;

import java.util.Collection;

public interface Manager <T extends Base>{
    T create(String name);

    Collection<T> get(String arg0, String arg1, String arg2) throws NotFoundException;

    T update(T object);

    void delete(T object) throws DBException;
}
