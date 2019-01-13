package ru.otus.Managers;

import org.springframework.stereotype.Service;
import ru.otus.Dao.AuthorDao;
import ru.otus.Dao.BookDao;
import ru.otus.Dao.GenreDao;
import ru.otus.Domain.Author;
import ru.otus.Exceptions.DataBaseException;
import ru.otus.Exceptions.NotFoundException;

import java.util.Collection;

@Service
public class AuthorManager implements Manager<Author> {
    private AuthorDao authorDao;

    public AuthorManager(AuthorDao authorDao) {
        this.authorDao = authorDao;
    }

    @Override
    public Author create(String name) {
        return new Author(name);
    }

    @Override
    public void save(Author author) {
        authorDao.save(author);
    }

    @Override
    public Collection<Author> get(String name, String genre, String book) throws NotFoundException {
        return null;
    }

    @Override
    public int update(Author author) throws DataBaseException {
        int res = authorDao.update(author);
        if (res > 0) {
            return  res;
        } else {
            throw new DataBaseException("Cannot update Author");
        }
    }

    @Override
    public int delete(Author author) throws DataBaseException {
        int res = authorDao.delete(author);
        if (res > 0) {
            return res;
        } else {
            throw new DataBaseException("Cannot delete Genre");
        }
    }
}
