package ru.otus.ManagersTest;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import ru.otus.Dao.GenreDao;
import ru.otus.Domain.Genre;
import ru.otus.Exceptions.DataBaseException;
import ru.otus.Exceptions.NotFoundException;

import java.util.ArrayList;
import java.util.Collection;

@Service
public class GenreManager implements Manager<Genre> {
    private GenreDao genreDao;

    public GenreManager(GenreDao genreDao) {
        this.genreDao = genreDao;
    }

    @Override
    public Genre create(String name) {
        Genre genre = new Genre(name);
        genreDao.save(genre);
        return genre;
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
