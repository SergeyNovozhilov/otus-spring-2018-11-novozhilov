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

import static junit.framework.TestCase.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class AuthorDaoJdbcTests {
    @Autowired
    private AuthorDao authorDao;
    @Autowired
    private BookDao bookDao;
    @Autowired
    private GenreDao genreDao;

    @Test
    public void saveAndGetTest() {
        String name = "Jack London";
        Author expected = new Author(name);
        authorDao.save(expected);
        Collection<Author> actual = authorDao.getByName(name);
        assertTrue(actual.size() == 1);
        assertTrue(actual.contains(expected));
    }

    @Test
    public void updateAuthorTest() {
        String name = "Jerome Jerome";
        Author author = new Author(name);
        String genreName = "Humor";
        Genre genre = new Genre(genreName);
        genreDao.save(genre);
        author.addGenre(genre);
        authorDao.save(author);

        String newName = "Jerome K. Jerome";
        String genreNewName = "Novel";
        Genre newGenre = new Genre(genreNewName);
        genreDao.save(newGenre);
        author.setName(newName);
        author.getGenres().remove(genre);
        author.addGenre(newGenre);
        int count = authorDao.update(author);
        assertTrue(count == 1);

        Collection<Author> actual = authorDao.getByName(name);
        assertTrue(actual.isEmpty());

        actual = authorDao.getByName(newName);
        assertTrue(actual.contains(author));
    }

    @Test
    public void deleteAuthorTest() {
        String name = "Jerome K. Jerome";
        Author author = new Author(name);
        authorDao.save(author);
        Collection<Author> actual = authorDao.getByName(name);
        assertTrue(actual.contains(author));

        int count = authorDao.delete(author);
        assertTrue(count == 1);

        Collection<Author> actualAfterDelete = authorDao.getByName(name);
        assertTrue(actualAfterDelete.isEmpty());
    }

    @Test
    public void authorGetByGenreTest() {
        String genreName = "Romantic";
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
        Author expected = new Author("Jack London");
        authorDao.save(expected);

        Genre genre = new Genre("Novel");
        genreDao.save(genre);

        String bookTitle = "Book";
        Book book = new Book(bookTitle, genre);
        book.addAuthor(expected);
        bookDao.save(book);

        Collection<Author> actual = authorDao.getByBook(bookTitle);
        assertTrue(actual.size() == 1);
        assertTrue(actual.contains(expected));
    }

    @After
    public void cleanUp() {
        bookDao.deleteAll();
        authorDao.deleteAll();
        genreDao.deleteAll();

    }
}
