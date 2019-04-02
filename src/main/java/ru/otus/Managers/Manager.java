package ru.otus.managers;

import ru.otus.entities.Entity;
import ru.otus.exceptions.DataBaseException;
import ru.otus.exceptions.NotFoundException;

import java.util.Collection;

public interface Manager <T extends Entity>{
    T create(String name);

    Collection<T> get(String arg0, String arg1, String arg2) throws NotFoundException;

    T update(T object) throws DataBaseException;

    void delete(T object) throws DataBaseException;
}
