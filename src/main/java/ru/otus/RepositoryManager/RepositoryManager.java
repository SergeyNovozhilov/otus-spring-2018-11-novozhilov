package ru.otus.RepositoryManager;

import ru.otus.Dao.BaseDao;
import ru.otus.Domain.Base;

public interface RepositoryManager {
    <T extends BaseDao> T getDao(Class clazz);
}
