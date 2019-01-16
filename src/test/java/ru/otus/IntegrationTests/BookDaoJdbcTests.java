package ru.otus.IntegrationTests;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit4.SpringRunner;
import ru.otus.Dao.BookDao;
import ru.otus.DaoImpl.BookDaoJdbc;
import ru.otus.Domain.Book;

import java.util.Collection;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertTrue;

@RunWith(SpringRunner.class)
@JdbcTest
@Import(BookDaoJdbc.class)
public class BookDaoJdbcTests {
    @Autowired
    private BookDao bookDao;
    private String title = "Book";
    private Book expected;

    @Before
    public void setUp() {
        expected = new Book(title);
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
        expected.setTitle(newTitle);
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

        Book actualAfterDelete = bookDao.getById(expected.getId());
        assertTrue(actualAfterDelete == null);
    }

    @Test
    public void bookGetByGenreTest() {
        String genreName = "Thriller";
        String bookTitle = "Book by Steven King and Ambrose Bierce";

        Collection<Book> actual = bookDao.getByGenre(genreName);
        assertTrue(actual.size() == 1);
        assertEquals(actual.iterator().next().getTitle(), bookTitle);
    }

    @Test
    public void bookGetByAuthorTest() {
        String authorName = "Jack London";
        String bookTitle = "Book by Jack London";

        Collection<Book> actual = bookDao.getByAuthor(authorName);
        assertTrue(actual.size() == 1);
        assertEquals(actual.iterator().next().getTitle(), bookTitle);
    }
    
}
