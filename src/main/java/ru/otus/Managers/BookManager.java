package ru.otus.Managers;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import ru.otus.Dao.AuthorDao;
import ru.otus.Dao.BookDao;
import ru.otus.Dao.GenreDao;
import ru.otus.Domain.Author;
import ru.otus.Domain.Book;
import ru.otus.Domain.Genre;
import ru.otus.Exceptions.DataBaseException;
import ru.otus.Exceptions.NotFoundException;

import java.util.ArrayList;
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
        Genre genre = genreDao.getByName(genreName);
        if (genre == null) {
            genre = new Genre(genreName);
            genreDao.save(genre);
        }
        book.setGenre(genre);
        return book;
    }

    public Book addAuthor(Book book, String authorName) {
        Author author = authorDao.getByName(authorName);
        if (author == null) {
            author = new Author(authorName);
            authorDao.save(author);
        }
        book.addAuthor(author);
        return book;
    }

    @Override
    public Collection<Book> get(String title, String genre, String author) throws NotFoundException {
        Collection<Book> books = new ArrayList<>();
        Collection<Book> booksByTitle = null;
        Collection<Book> booksByGenre = null;
        Collection<Book> booksByAuthor = null;

        if (StringUtils.isBlank(title) && StringUtils.isBlank(genre) && StringUtils.isBlank(author)) {
            return returnBooks(bookDao.getAll());
        }

        if (StringUtils.isNotBlank(title) && StringUtils.isNotBlank(author) && StringUtils.isBlank(genre)) {
            booksByTitle = bookDao.getByTitle(title);
            booksByTitle.forEach(b -> {
                if (b.getAuthors().stream().filter(a -> a.getName().equals(author)).findAny().orElse(null) != null) {
                    books.add(b);
                }
            });
            return returnBooks(books);
        }

        if (StringUtils.isNotBlank(title)) {
            booksByTitle = bookDao.getByTitle(title);
            if (!booksByTitle.isEmpty() && books.isEmpty()) {
                books.addAll(booksByTitle);
            }
        }
        if (StringUtils.isNotBlank(genre)) {
            booksByGenre = bookDao.getByGenre(genre);
            if (!booksByGenre.isEmpty() && books.isEmpty()) {
                books.addAll(booksByGenre);
            }
        }
        if (StringUtils.isNotBlank(author)) {
            booksByAuthor = bookDao.getByAuthor(author);
            if (!booksByAuthor.isEmpty() && books.isEmpty()) {
                books.addAll(booksByAuthor);
            }
        }

        if (booksByTitle != null) {
            books.retainAll(booksByTitle);
        }
        if (booksByGenre != null) {
            books.retainAll(booksByGenre);
        }
        if (booksByAuthor != null) {
            books.retainAll(booksByAuthor);
        }

        return returnBooks(books);
    }

    @Override
    public int update(Book book) throws DataBaseException {
        Book b = bookDao.update(book);
        if (b != null) {
            return  1;
        }
        return 0;
    }

    @Override
    public int delete(Book book) throws DataBaseException {
        bookDao.delete(book);
        return 1;
    }

    private Collection<Book> returnBooks(Collection<Book> books) throws NotFoundException{
        if (books == null || books.isEmpty()) {
            throw new NotFoundException("No Books were found.");
        }
        return books;
    }
}
