package ru.otus.IntegrationTests;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit4.SpringRunner;
import ru.otus.Dao.GenreDao;
import ru.otus.DaoImpl.GenreDaoJdbc;
import ru.otus.Domain.Genre;

import java.util.Collection;

import static junit.framework.TestCase.*;

@RunWith(SpringRunner.class)
@JdbcTest
@Import(GenreDaoJdbc.class)
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
        Genre genre = new Genre("Thriller");

        Collection<Genre> actual = genreDao.getByAuthor("Jerome C. Jerome");
        assertTrue(actual.size() == 1);
        assertTrue(actual.contains(genre));
    }

    @Test
    public void genreGetByBookTest() {
        Genre genre = new Genre("Novel");

        Genre actual = genreDao.getByBook("Book by Jack London");
        assertEquals(genre, actual);
    }
}
