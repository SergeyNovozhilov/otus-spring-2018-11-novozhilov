package ru.otus.IntegrationTests;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit4.SpringRunner;
import ru.otus.Dao.AuthorDao;
import ru.otus.Domain.Author;

import java.util.Collection;

import static junit.framework.TestCase.*;

@RunWith(SpringRunner.class)
@JdbcTest
@Import(AuthorDaoJdbc.class)
public class AuthorDaoJdbcTests {
    @Autowired
    private AuthorDao authorDao;

    private String authorName = "Mark Twain";
    private Author author;

    @Before
    public void setUp() {
        author = new Author(authorName);
        authorDao.save(author);
    }

    @Test
    public void saveAndGetTest() {
        Author actual = authorDao.getByName(authorName);
        assertEquals(actual, author);
    }

    @Test
    public void updateAuthorTest() {
        String newName = "Samuel Langhorne Clemens";
        author.setName(newName);
        int count = authorDao.update(author);
        assertTrue(count == 1);

        Author actual = authorDao.getByName(authorName);
        assertTrue(actual == null);

        actual = authorDao.getByName(newName);
        assertEquals(actual, author);
    }

    @Test
    public void deleteAuthorTest() {
        int count = authorDao.delete(author);
        assertTrue(count == 1);

        Author actualAfterDelete = authorDao.getById(author.getId());
        assertTrue(actualAfterDelete == null);
    }

    @Test
    public void authorGetByGenreTest() {
        String genreName = "Thriller";

        String authorName1 = "Steven King";
        String authorName2 = "Ambrose Bierce";
        String authorName3 = "Jerome C. Jerome";

        Collection<Author> actual = authorDao.getByGenre(genreName);
        assertTrue(actual.size() == 3);
        assertNotNull(actual.stream().map(Author::getName).filter(name -> name.equals(authorName1)).findAny().orElse(null));
        assertNotNull(actual.stream().map(Author::getName).filter(name -> name.equals(authorName2)).findAny().orElse(null));
        assertNotNull(actual.stream().map(Author::getName).filter(name -> name.equals(authorName3)).findAny().orElse(null));
    }

    @Test
    public void authorGetByBookTest() {
        String bookTitle = "Book by Jack London";

        Collection<Author> actual = authorDao.getByBook(bookTitle);
        assertTrue(actual.size() == 1);
        assertEquals(actual.iterator().next().getName(), "Jack London");
    }
}
