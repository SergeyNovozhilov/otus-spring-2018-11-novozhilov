package ru.otus.IntegrationTests;

import org.apache.commons.lang3.StringUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;
import ru.otus.Entities.Author;
import ru.otus.Entities.Book;
import ru.otus.Entities.Genre;
import ru.otus.Repositories.GenreRepository;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class GenreTests {

    @Autowired
    private TestEntityManager testEntityManager;

    @Autowired
    private GenreRepository genreRepository;

    @Test
    public void getAll() {
        List<Genre> expected = Arrays.asList(new Genre("Novel"), new Genre("Humor"));
        expected.forEach(e -> testEntityManager.persist(e));
        testEntityManager.flush();
        Collection<Genre> actual = genreRepository.findAll();
        assertEquals(actual, expected);
    }

    @Test
    public void getByName() {
        Genre expected = new Genre("Novel");
        testEntityManager.persistAndFlush(expected);
        Genre actual = genreRepository.findByName(expected.getName());
        assertEquals(actual, expected);
    }

    @Test
    public void getById() {
        Genre expected = new Genre("Novel");
        testEntityManager.persistAndFlush(expected);
        Genre actual = genreRepository.findById(expected.getId()).orElse(new Genre());
        assertEquals(actual, expected);
    }

    @Test
    public void getByAuthor() {
        String expectedGenreName = "Novel";
        String bookTitle = "BookDto by Jack London";
        String author = "Jack London";
        Book book = createBook(bookTitle, expectedGenreName, author);
        book = testEntityManager.persistAndFlush(book);
        Book b = testEntityManager.find(Book.class, book.getId());
        Collection<Genre> actual = genreRepository.findByAuthor(author);
        assertTrue(actual.size() == 1);
        assertEquals(actual.iterator().next().getName(), expectedGenreName);
    }

    @Test
    public void getByBook() {
        String expectedGenreName = "Novel";
        String book = "BookDto by Jack London";
        testEntityManager.persistAndFlush(createBook(book, expectedGenreName, ""));
        Genre actual = genreRepository.findByBook(book);
        assertEquals(actual.getName(), expectedGenreName);
    }

    @Test
    public void save() {
        Genre expected = new Genre("Humor");
        genreRepository.save(expected);
        Genre actual = testEntityManager.find(Genre.class, expected.getId());
        assertEquals(actual, expected);
    }

    @Test
    public void delete() {
        Genre expected = new Genre("Novel");
        testEntityManager.persistAndFlush(expected);
        Genre actual = genreRepository.findById(expected.getId()).orElse(new Genre());
        assertEquals(actual, expected);
        genreRepository.delete(expected);
        actual = testEntityManager.find(Genre.class, expected.getId());
        assertNull(actual);
    }

    @Test
    public void update() {
        String newName = "Romantic";
        Genre expected = new Genre("Novel");
        genreRepository.save(expected);
        Genre actual = testEntityManager.find(Genre.class, expected.getId());
        assertEquals(actual, expected);
        expected.setName(newName);
        genreRepository.save(expected);
        actual = testEntityManager.find(Genre.class, expected.getId());
        assertEquals(actual.getName(), expected.getName());
    }

    private Book createBook(String bookStr, String genreStr, String authorStr) {
        Book book = new Book(bookStr);
        Genre genre = null;
        if (StringUtils.isNotBlank(genreStr)) {
            genre = new Genre(genreStr);
            testEntityManager.persistAndFlush(genre);
            book.setGenre(genre);
        }
        if (StringUtils.isNotBlank(authorStr)) {
            Author author = new Author(authorStr);
            testEntityManager.persistAndFlush(author);
            book.addAuthor(author);
        }
        return book;
    }
}