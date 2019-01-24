package ru.otus.IntegrationTests;

import org.apache.commons.lang3.StringUtils;
import org.h2.tools.Console;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit4.SpringRunner;
import ru.otus.DaoImpl.AuthorDaoJpa;
import ru.otus.DaoImpl.BookDaoJpa;
import ru.otus.Domain.Author;
import ru.otus.Domain.Book;
import ru.otus.Domain.Genre;

import java.sql.SQLException;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Import({AuthorDaoJpa.class, BookDaoJpa.class})
public class AuthorDaoTests {

	@Autowired
	private TestEntityManager testEntityManager;

	@Autowired
	private AuthorDaoJpa authorDaoJpa;

	@Autowired
	private BookDaoJpa bookDaoJpa;

	@Test
	public void getAll() {
		List<Author> expected = Arrays.asList(new Author("Jack London"), new Author("Mark Twain"));
		expected.forEach(e -> testEntityManager.persist(e));
		testEntityManager.flush();
		Collection<Author> actual = authorDaoJpa.getAll();
		assertEquals(actual, expected);
	}

	@Test
	public void getByName() {
		Author expected = new Author("Steven King");
		testEntityManager.persistAndFlush(expected);
		Author actual = authorDaoJpa.getByName(expected.getName());
		assertEquals(actual, expected);
	}

	@Test
	public void getById() {
		Author expected = new Author("Steven King");
		testEntityManager.persistAndFlush(expected);
		Author actual = authorDaoJpa.getById(expected.getId());
		assertEquals(actual, expected);
	}

	@Test
	public void getByBook() throws SQLException {
		String bookTitle = "Book by Jack London";
		String authorName = "Jack London";
		createAndPersistBook(bookTitle, "Genre", authorName);
		Collection<Author> actual = authorDaoJpa.getByBook(bookTitle);
		assertTrue(actual.size() == 1);
		assertEquals(actual.iterator().next().getName(), "Jack London");
		assertTrue(true);
	}

	@Test
	public void getByGenre() throws SQLException {
		String genreName = "Thriller";
		String authorName = "Steven King";
		createAndPersistBook("Book", genreName, authorName);
		Collection<Author> actual = authorDaoJpa.getByGenre(genreName);
		assertTrue(actual.size() == 1);
		assertNotNull(actual.stream().map(Author::getName).filter(name -> name.equals(authorName)).findAny().orElse(null));
	}

	@Test
	public void save() {
		Author expected = new Author("Steven King");
		authorDaoJpa.save(expected);
		Author actual = testEntityManager.find(Author.class, expected.getId());
		assertEquals(actual, expected);
	}

	@Test
	public void delete() {
		Author expected = new Author("Steven King");
		testEntityManager.persistAndFlush(expected);
		Author actual = authorDaoJpa.getById(expected.getId());
		assertEquals(actual, expected);
		authorDaoJpa.delete(expected);
		actual = testEntityManager.find(Author.class, expected.getId());
		assertNull(actual);
	}

	@Test
	public void update() {
		String newName = "Steven Not King";
		Author expected = new Author("Steven King");
		authorDaoJpa.save(expected);
		Author actual = testEntityManager.find(Author.class, expected.getId());
		assertEquals(actual, expected);
		expected.setName(newName);
		authorDaoJpa.update(expected);
		actual = testEntityManager.find(Author.class, expected.getId());
		assertEquals(actual.getName(), expected.getName());
	}

	private Book createAndPersistBook(String bookStr, String genreStr, String authorStr) {
		Book book = new Book(bookStr);
		Genre genre = null;
		if (StringUtils.isNotBlank(genreStr)) {
			genre = new Genre(genreStr);
			book.setGenre(genre);
		}
		if (StringUtils.isNotBlank(authorStr)) {
			Author author = new Author(authorStr);
			book.addAuthor(author);
		}
		testEntityManager.persistAndFlush(book);
		return book;
	}
}
