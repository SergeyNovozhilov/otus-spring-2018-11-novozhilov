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
import java.util.Collections;

import static junit.framework.TestCase.*;

@RunWith(SpringRunner.class)
@JdbcTest
@Import(GenreDao.class)
public class GenreDaoJdbcTests {
    @Autowired
    private GenreDao genreDao;

    private String genreName = "Romantic";
    private Genre genreExpected;

    @Before
    public void setUp() {
        genreExpected = new Genre(genreName);
        genreDao.save(genreExpected);
    }

    @Test
    public void saveAndGetTest() {
        Genre actualGenre = genreDao.getByName(genreName);
        assertEquals(genreExpected, actualGenre);
    }

    @Test
    public void updateGenreTest() {
        String newName = "Not Romantic";

        genreExpected.setName(newName);
        int count = genreDao.update(genreExpected);
        assertTrue(count == 1);

        Genre actualGenreAfterUpdate = genreDao.getByName(genreName);
        assertTrue(actualGenreAfterUpdate == null);

        actualGenreAfterUpdate = genreDao.getByName(newName);
        assertFalse(actualGenreAfterUpdate == null);

        assertEquals(actualGenreAfterUpdate.getName(), newName);
    }

    @Test
    public void deleteGenreTest() {
        int count = genreDao.delete(genreExpected);
        assertTrue(count == 1);

        Genre actualGenreAfterDelete = genreDao.getByName(genreName);
        assertTrue(actualGenreAfterDelete == null);
    }

    @Test
    public void genreGetByAuthorTest() {
        Genre genre1 = new Genre("Humor");
        genreDao.save(genre1);

        Genre genre2 = new Genre("Not humor");
        genreDao.save(genre2);

        String authorName = "Famous Author";
        Author author = new Author(authorName);
        author.addGenre(genre1);
        author.addGenre(genre2);
        authorDao.save(author);

        Collection<Genre> actual = genreDao.getByAuthor(authorName);
        assertTrue(actual.size() == 2);
        assertTrue(actual.contains(genre1));
        assertTrue(actual.contains(genre2));
    }

    @Test
    public void genreGetByBookTest() {
        String bookTitle = "Book";
        Book book = new Book(bookTitle, genreExpected);
        bookDao.save(book);

        Genre actual = genreDao.getByBook(bookTitle);
        assertEquals(genreExpected, actual);
    }
}
