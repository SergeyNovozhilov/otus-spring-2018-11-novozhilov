package ru.otus.integrationtests;

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
import ru.otus.repositories.BookRepository;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class BookTests {

	@Autowired
	private TestEntityManager testEntityManager;

	@Autowired
	private BookRepository bookRepository;

	@Test
	public void getAll() {
		List<Book> expected = Arrays.asList(new Book("Book"), new Book("Book1"));
		expected.forEach(e -> testEntityManager.persist(e));
		testEntityManager.flush();
		Collection<Book> actual = bookRepository.findAll();
		assertTrue(actual.containsAll(expected));
	}

	@Test
	public void getByTitle() {
		Book expected = new Book("Book by Steven King");
		testEntityManager.persistAndFlush(expected);
		Collection<Book> actual = bookRepository.findByTitle(expected.getTitle());
		assertTrue(actual.size() == 1);
		assertTrue(actual.contains(expected));
	}

	@Test
	public void getById() {
		Book expected = new Book("BookDto by Steven King");
		testEntityManager.persistAndFlush(expected);
		Book actual = bookRepository.findById(expected.getId()).orElse(new Book());
		assertEquals(actual, expected);
	}

	@Test
	public void getByAuthor() {
		String bookTitle = "BookDto by Jack London";
		String authorName = "Jack London";
		createAndPersistBook(bookTitle, "GenreDto", authorName);
		Collection<Book> actual = bookRepository.findByAuthor(authorName);
		assertTrue(actual.size() == 1);
		assertEquals(actual.iterator().next().getTitle(), "BookDto by Jack London");
		assertTrue(true);
	}

	@Test
	public void getByGenre() {
		String genreName = "Thriller";
		String authorName = "Steven King";
		String bookTitle = "BookDto";
		createAndPersistBook(bookTitle, genreName, authorName);
		Collection<Book> actual = bookRepository.findByGenre(genreName);
		assertTrue(actual.size() == 1);
		assertNotNull(actual.stream().map(Book::getTitle).filter(name -> name.equals(bookTitle)).findAny().orElse(null));
	}

	@Test
	public void save() {
		Book expected = new Book("BookDto by Steven King");
		bookRepository.save(expected);
		Book actual = testEntityManager.find(Book.class, expected.getId());
		assertEquals(actual, expected);
	}

	@Test
	public void delete() {
		Book expected = createAndPersistBook("BookDto", "Thriller", "Steven King");
		Book actual = bookRepository.findById(expected.getId()).orElse(new Book());
		assertEquals(actual, expected);
		bookRepository.delete(expected);
		actual = testEntityManager.find(Book.class, expected.getId());
		assertNull(actual);
	}


	@Test
	public void update() {
		String newTitle = "BookDto by Steven Not King";
		Book expected = new Book("BookDto by Steven King");
		bookRepository.save(expected);
		Book actual = testEntityManager.find(Book.class, expected.getId());
		assertEquals(actual, expected);
		expected.setTitle(newTitle);
		bookRepository.save(expected);
		actual = testEntityManager.find(Book.class, expected.getId());
		assertEquals(actual.getTitle(), expected.getTitle());
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
		testEntityManager.persistAndFlush(book);
		return book;
	}
}