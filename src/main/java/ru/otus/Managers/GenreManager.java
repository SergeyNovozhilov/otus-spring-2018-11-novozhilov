package ru.otus.Managers;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import ru.otus.Dao.AuthorDao;
import ru.otus.Dao.BookDao;
import ru.otus.Dao.GenreDao;
import ru.otus.Domain.Genre;
import ru.otus.Exceptions.DataBaseException;
import ru.otus.Exceptions.NotFoundException;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

@Service
public class GenreManager implements Manager<Genre> {
    private AuthorDao authorDao;
    private GenreDao genreDao;
    private BookDao bookDao;

    public GenreManager(AuthorDao authorDao, GenreDao genreDao, BookDao bookDao) {
        this.authorDao = authorDao;
        this.genreDao = genreDao;
        this.bookDao = bookDao;
    }

    @Override
    public Genre create(String name) {
        return new Genre(name);
    }

    @Override
    public void save(Genre genre) {
        genreDao.save(genre);
    }

    @Override
    public Collection<Genre> get(String name, String author, String book) throws NotFoundException {
        Collection<Genre> genres = new ArrayList<>();
        if (StringUtils.isBlank(name) && StringUtils.isBlank(author) && StringUtils.isBlank(book)) {
            genreDao.getAll();
        } else {
            if (StringUtils.isNotBlank(name)) {
                Genre genre = genreDao.getByName(name);
                if (genre == null) {
                    throw new NotFoundException("Genre with name: " + name);
                }
                genres.add(genre);
            } else if (StringUtils.isNotBlank(book)) {
                Genre genre = genreDao.getByBook(book);
                if (genre == null) {
                    throw new NotFoundException("Genre of book: " + name + " not found");
                }
                genres.add(genre);
            } else if (StringUtils.isNotBlank(author)) {
                genres.addAll(genreDao.getByAuthor(author));
                if (genres.isEmpty()) {
                    throw new NotFoundException("Genre of author: " + author + " not found");
                }
            }
        }
        return genres;
    }

    @Override
    public int update(Genre genre) throws DataBaseException {
        int res = genreDao.update(genre);
        if (res > 0) {
            return  res;
        } else {
            throw new DataBaseException("Cannot update Genre");
        }
    }

    @Override
    public int delete(Genre genre) throws DataBaseException {
        int res = genreDao.delete(genre);
        if (res > 0) {
            return res;
        } else {
            throw new DataBaseException("Cannot delete Genre");
        }
    }
}
