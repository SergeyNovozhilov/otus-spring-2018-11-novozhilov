package ru.otus.IntegrationTests;

import org.apache.commons.lang3.StringUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;
import ru.otus.entities.Author;
import ru.otus.entities.Book;
import ru.otus.entities.Genre;

import java.util.*;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class AuthorTests {

	@Autowired
	private TestEntityManager testEntityManager;

	@Autowired
	private AuthorRepository authorRepository;

	@Test
	public void getAll() {
		List<Author> expected = Arrays.asList(new Author("Jack London"), new Author("Mark Twain"));
		expected.forEach(e -> testEntityManager.persist(e));
		testEntityManager.flush();
		List<Author> actual = new ArrayList(authorRepository.findAll());
		assertTrue(expected.size() == actual.size());
		expected.forEach( e -> {
			Author a = actual.stream().filter(x -> x.getId().equals(e.getId())).findFirst().orElse(null);
			if (a == null) {
				fail();
			}
			assertEquals(e.getName(), a.getName());
		});
	}

	@Test
	public void getByName() {
		Author expected = new Author("Steven King");
		testEntityManager.persistAndFlush(expected);
		Author actual = authorRepository.findByName(expected.getName());
		assertEquals(expected, actual);
	}

	@Test
	public void getById() {
		Author expected = new Author("Steven King");
		testEntityManager.persistAndFlush(expected);
		Author actual = authorRepository.findById(expected.getId()).orElse(new Author());
		assertEquals(actual, expected);
	}

	@Test
	public void getByBook() {
		String bookTitle = "BookDto by Jack London";
		String authorName = "Jack London";
		createAndPersistBook(bookTitle, "GenreDto", authorName);
		Collection<Author> actual = authorRepository.findByBook(bookTitle);
		assertTrue(actual.size() == 1);
		Author a = actual.stream().findAny().orElse(new Author());
		assertEquals(a.getName(), authorName);
		assertTrue(true);
	}

	@Test
	public void getByGenre() {
		String genreName = "Thriller";
		String authorName = "Steven King";
		createAndPersistBook("BookDto", genreName, authorName);
		Collection<Author> actual = authorRepository.findByGenre(genreName);
		assertTrue(actual.size() == 1);
		assertNotNull(actual.stream().map(Author::getName).filter(name -> name.equals(authorName)).findAny().orElse(null));
	}

	@Test
	public void save() {
		Author expected = new Author("Steven King");
		authorRepository.save(expected);
		Author actual = testEntityManager.find(Author.class, expected.getId());
		assertEquals(actual, expected);
	}

	@Test
	public void delete() {
		Author expected = new Author("Steven King");
		testEntityManager.persistAndFlush(expected);
		Author actual = authorRepository.findById(expected.getId()).orElse(new Author());
		assertEquals(actual, expected);
		authorRepository.delete(expected);
		actual = testEntityManager.find(Author.class, expected.getId());
		assertNull(actual);
	}

	@Test
	public void update() {
		String newName = "Steven Not King";
		Author expected = new Author("Steven King");
		authorRepository.save(expected);
		Author actual = testEntityManager.find(Author.class, expected.getId());
		assertEquals(actual, expected);
		expected.setName(newName);
		authorRepository.save(expected);
		actual = testEntityManager.find(Author.class, expected.getId());
		assertEquals(actual.getName(), expected.getName());
	}

	private Book createAndPersistBook(String bookStr, String genreStr, String authorStr) {
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
		testEntityManager.persist(book);
		testEntityManager.flush();
		return book;
	}
}
