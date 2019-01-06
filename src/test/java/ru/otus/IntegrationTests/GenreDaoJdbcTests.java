package ru.otus.IntegrationTests;

import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
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
@SpringBootTest
public class GenreDaoJdbcTests {
    @Autowired
    private AuthorDao authorDao;
    @Autowired
    private BookDao bookDao;
    @Autowired
    private GenreDao genreDao;

    @Test
    public void saveAndGetTest() {
        String genreName = "Romantic";
        Genre genreExpected = new Genre(genreName);
        genreDao.save(genreExpected);
        Genre actualGenre = genreDao.getByName(genreName);
        assertEquals(genreExpected, actualGenre);
    }

    @Test
    public void updateGenreTest() {
        String genreName = "Romantic";
        String newName = "Not Romantic";
        Genre genre = new Genre(genreName);
        genreDao.save(genre);
        Genre actualGenre = genreDao.getByName(genreName);
        assertEquals(genre, actualGenre);

        actualGenre.setName(newName);
        int count = genreDao.update(actualGenre);
        assertTrue(count == 1);

        Genre actualGenreAfterUpdate = genreDao.getByName(genreName);
        assertTrue(actualGenreAfterUpdate == null);

        actualGenreAfterUpdate = genreDao.getByName(newName);
        assertFalse(actualGenreAfterUpdate == null);

        assertEquals(actualGenreAfterUpdate.getName(), newName);
    }

    @Test
    public void deleteGenreTest() {
        String genreName = "Romantic";
        Genre genre = new Genre(genreName);
        genreDao.save(genre);
        Genre actualGenre = genreDao.getByName(genreName);
        assertEquals(genre, actualGenre);

        int count = genreDao.delete(actualGenre);
        assertTrue(count == 1);

        Genre actualGenreAfterDelete = genreDao.getByName(genreName);
        assertTrue(actualGenreAfterDelete == null);
    }

    @Test
    public void genreGetByAuthorTest() {
        Genre genre1 = new Genre("Romantic");
        genreDao.save(genre1);

        Genre genre2 = new Genre("Not Romantic");
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
        Genre expected = new Genre("Romantic");
        genreDao.save(expected);

        String bookTitle = "Book";
        Book book = new Book(bookTitle, expected);
        bookDao.save(book);

        Genre actual = genreDao.getByBook(bookTitle);
        assertEquals(expected, actual);
    }

    @After
    public void cleanUp() {
        bookDao.deleteAll();
        authorDao.deleteAll();
        genreDao.deleteAll();
    }
}
