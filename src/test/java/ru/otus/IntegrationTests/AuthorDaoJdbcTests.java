package ru.otus.IntegrationTests;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.shell.jline.InteractiveShellApplicationRunner;
import org.springframework.test.context.junit4.SpringRunner;
import ru.otus.Dao.AuthorDao;
import ru.otus.Dao.BookDao;
import ru.otus.Dao.GenreDao;
import ru.otus.Domain.Author;
import ru.otus.Domain.Book;
import ru.otus.Domain.Genre;

import java.util.Collection;

import static junit.framework.TestCase.*;

@RunWith(SpringRunner.class)
@JdbcTest
@Import(AuthorDao.class)
public class AuthorDaoJdbcTests {
    @Autowired
    private AuthorDao authorDao;

    private String genreName = "Humor";
    private Genre genre;
    private String authorName = "Jerome K. Jerome";
    private Author author;

    @Before
    public void setUp() {
        author = new Author(authorName);
        genre = new Genre(genreName);
        genreDao.save(genre);
        author.addGenre(genre);
        authorDao.save(author);
    }

    @Test
    public void saveAndGetTest() {
        Collection<Author> actual = authorDao.getByName(authorName);
        assertTrue(actual.size() == 1);
        assertTrue(actual.contains(author));
    }

    @Test
    public void updateAuthorTest() {
        String newName = "Jerome Jerome";
        String genreNewName = "Novel";
        Genre newGenre = new Genre(genreNewName);
        genreDao.save(newGenre);
        author.setName(newName);
        author.getGenres().remove(genre);
        author.addGenre(newGenre);
        int count = authorDao.update(author);
        assertTrue(count == 1);

        Collection<Author> actual = authorDao.getByName(authorName);
        assertTrue(actual.isEmpty());

        actual = authorDao.getByName(newName);
        assertTrue(actual.contains(author));
    }

    @Test
    public void deleteAuthorTest() {
        int count = authorDao.delete(author);
        assertTrue(count == 1);

        Collection<Author> actualAfterDelete = authorDao.getByName(authorName);
        assertTrue(actualAfterDelete.isEmpty());
    }

    @Test
    public void authorGetByGenreTest() {
        String genreName = "Novel";
        Genre genre = new Genre(genreName);
        genreDao.save(genre);

        String authorName1 = "Famous Author";
        Author author1 = new Author(authorName1);
        author1.addGenre(genre);
        authorDao.save(author1);

        String authorName2 = "Another Famous Author";
        Author author2 = new Author(authorName2);
        author2.addGenre(genre);
        authorDao.save(author2);

        Collection<Author> actual = authorDao.getByGenre(genreName);
        assertTrue(actual.size() == 2);
        assertTrue(actual.contains(author1));
        assertTrue(actual.contains(author2));
    }

    @Test
    public void authorGetByBookTest() {
        String bookTitle = "Book";
        Book book = new Book(bookTitle, genre);
        book.addAuthor(author);
        bookDao.save(book);

        Collection<Author> actual = authorDao.getByBook(bookTitle);
        assertTrue(actual.size() == 1);
        assertTrue(actual.contains(author));
    }
}
