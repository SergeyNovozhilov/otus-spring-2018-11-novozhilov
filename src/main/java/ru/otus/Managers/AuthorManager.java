package ru.otus.Managers;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import ru.otus.Dao.AuthorDao;
import ru.otus.Domain.Author;
import ru.otus.Exceptions.DataBaseException;
import ru.otus.Exceptions.NotFoundException;

import java.util.ArrayList;
import java.util.Collection;

@Service
public class AuthorManager implements Manager<Author> {
    private AuthorDao authorDao;

    public AuthorManager(AuthorDao authorDao) {
        this.authorDao = authorDao;
    }

    @Override
    public Author create(String name) {
        Author author = new Author(name);
        authorDao.save(author);
        return author;
    }

    @Override
    public Collection<Author> get(String name, String genre, String book) throws NotFoundException {
        Collection<Author> authors = new ArrayList<>();
        if (StringUtils.isBlank(name) && StringUtils.isBlank(genre) && StringUtils.isBlank(book)) {
            authors.addAll(authorDao.getAll());
            if (authors == null || authors.isEmpty()) {
                throw new NotFoundException("No Authors not found");
            }
            return authors;
        } else {
            if (StringUtils.isNotBlank(name)) {
                Author author = authorDao.getByName(name);
                if (author == null) {
                    throw new NotFoundException("Author with name: " + name + " not found");
                }
                authors.add(author);
            } else if (StringUtils.isNotBlank(book)) {
                authors.addAll(authorDao.getByBook(book));
                if (authors.isEmpty()) {
                    throw new NotFoundException("Authors of book: " + book + " not found");
                }
            } else if (StringUtils.isNotBlank(genre)) {
                authors.addAll(authorDao.getByGenre(genre));
                if (authors.isEmpty()) {
                    throw new NotFoundException("No authors with genre: " + genre);
                }
            }
        }
        return authors;
    }

    @Override
    public Author update(Author author) {
        return authorDao.update(author);
    }

    @Override
    public void delete(Author author) {
        authorDao.delete(author);
    }
}
