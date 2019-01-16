package ru.otus.IntegrationTests;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit4.SpringRunner;
import ru.otus.Dao.AuthorDao;
import ru.otus.Dao.BookDao;
import ru.otus.Dao.GenreDao;
import ru.otus.Domain.Author;
import ru.otus.Domain.Book;
import ru.otus.Domain.Genre;

import java.util.Collection;

import static junit.framework.TestCase.assertTrue;

@RunWith(SpringRunner.class)
@JdbcTest
@Import(BookDao.class)
public class BookDaoJdbcTests {
    @Autowired
    private BookDao bookDao;

    private Genre genre;
    private String genreName = "Humor";
    private String title = "Book";
    private Book expected;

    @Before
    public void setUp() {
        genre = new Genre(genreName);
        genreDao.save(genre);
        expected = new Book(title, genre);
        bookDao.save(expected);
    }

    @Test
    public void saveAndGetTest() {
        Collection<Book> actual = bookDao.getByTitle(title);
        assertTrue(actual.size() == 1);
        assertTrue(actual.contains(expected));
    }

    @Test
    public void updateBookTest() {
        String newTitle = "The best book";
        String genreNewName = "Novel";
        Genre newGenre = new Genre(genreNewName);
        genreDao.save(newGenre);
        expected.setTitle(newTitle);
        expected.setGenre(newGenre);
        int count = bookDao.update(expected);
        assertTrue(count == 1);

        Collection<Book> actual = bookDao.getByTitle(title);
        assertTrue(actual.isEmpty());

        actual = bookDao.getByTitle(newTitle);
        assertTrue(actual.contains(expected));
    }

    @Test
    public void deleteBookTest() {
        int count = bookDao.delete(expected);
        assertTrue(count == 1);

        Collection<Book> actualAfterDelete = bookDao.getByTitle(title);
        assertTrue(actualAfterDelete.isEmpty());
    }

    @Test
    public void bookGetByGenreTest() {
        String genreName = "Thriller";
        Genre genre = new Genre(genreName);
        genreDao.save(genre);
        String bookTitle1 = "Good book";
        Book book1 = new Book(bookTitle1, genre);
        bookDao.save(book1);

        String bookTitle2 = "Another Good Book";
        Book book2 = new Book(bookTitle2, genre);
        bookDao.save(book2);

        Collection<Book> actual = bookDao.getByGenre(genreName);
        assertTrue(actual.size() == 2);
        assertTrue(actual.contains(book1));
        assertTrue(actual.contains(book2));
    }

    @Test
    public void bookGetByAuthorTest() {
        String authorName = "Jack London";
        Author author = new Author(authorName);
        author.addGenre(genre);
        authorDao.save(author);

        Book expected = new Book("Thriller", genre);
        expected.addAuthor(author);
        bookDao.save(expected);

        Collection<Book> actual = bookDao.getByAuthor(authorName);
        assertTrue(actual.size() == 1);
        assertTrue(actual.contains(expected));
    }
    
}
