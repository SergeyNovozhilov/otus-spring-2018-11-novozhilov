package ru.otus.Managers;

import org.springframework.stereotype.Service;
import ru.otus.Dao.AuthorDao;
import ru.otus.Dao.BookDao;
import ru.otus.Dao.GenreDao;
import ru.otus.Domain.Author;
import ru.otus.Domain.Book;
import ru.otus.Domain.Genre;
import ru.otus.Exceptions.DataBaseException;
import ru.otus.Exceptions.NotFoundException;

import java.util.Collection;

@Service
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
    public Book create(String title) {
        Book book = new Book(title);
        bookDao.save(book);
        return book;
    }

    public Book addGenre(Book book, String genreName) {
        Genre genre = null;
        genre = genreDao.getByName(genreName);
        if (genre == null) {
            genre = new Genre(genreName);
            genreDao.save(genre);
        }
        book.setGenre(genre);
        return book;
    }

    public Book addAuthor(Book book, String authorName) {
        Author author = null;
        author = authorDao.getByName(authorName);
        if (author == null) {
            author = new Author(authorName);
            authorDao.save(author);
        }
        book.addAuthor(author);
        return book;
    }

    @Override
    public Collection<Book> get(String title, String genre, String author) throws NotFoundException {
        return null;
    }

    @Override
    public int update(Book book) throws DataBaseException {
        int res = bookDao.update(book);
        if (res > 0) {
            return  res;
        } else {
            throw new DataBaseException("Cannot update Book");
        }
    }

    @Override
    public int delete(Book book) throws DataBaseException {
        int res = bookDao.update(book);
        if (res > 0) {
            return  res;
        } else {
            throw new DataBaseException("Cannot delete Book");
        }
    }
}
