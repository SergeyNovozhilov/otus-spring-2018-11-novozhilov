package ru.otus.Managers;

import org.springframework.dao.DataAccessException;
import ru.otus.Dao.AuthorDao;
import ru.otus.Dao.BookDao;
import ru.otus.Dao.GenreDao;
import ru.otus.Domain.Author;
import ru.otus.Domain.Book;
import ru.otus.Domain.Genre;
import ru.otus.Exceptions.DataBaseException;

import java.util.Collection;

public class BookManager implements Manager<Book> {
    private AuthorDao authorDao;
    private GenreDao genreDao;
    private BookDao bookDao;

    public BookManager(AuthorDao authorDao, GenreDao genreDao, BookDao bookDao) {
        this.authorDao = authorDao;
        this.genreDao = genreDao;
        this.bookDao = bookDao;
    }
    @Override
    public Book create(String name) {
        return null;
    }

    public Book addGenre(Book book, String genreName) {
        Genre genre = null;
        try {
            genre = genreDao.getByName(genreName);
        } catch (DataAccessException e) {
            genre = new Genre(genreName);
            genreDao.save(genre);
        }
        book.setGenre(genre);
        return book;
    }

    public Book addAuthor(Book book, String authorName) {
        Author author = null;
        try {
            author = authorDao.getByName(authorName);
        } catch (DataAccessException e) {
            author = new Author(authorName);
            authorDao.save(author);
        }
        book.setGenre(genre);
        return book;
    }

    @Override
    public void save(Book book) {

    }

    @Override
    public Collection<Book> get(String arg0, String arg1, String arg2) throws DataBaseException {
        return null;
    }

    @Override
    public Book update(Book object) {
        return null;
    }

    @Override
    public int delete(Book object) throws DataBaseException {
        return 0;
    }
}
