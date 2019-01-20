package ru.otus.IntegrationTests;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit4.SpringRunner;
import ru.otus.DaoImpl.AuthorDaoJpa;
import ru.otus.Domain.Author;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@DataJpaTest
@Import(AuthorDaoJpa.class)
public class AuthorDaoJpaTest {

	@Autowired
	private TestEntityManager testEntityManager;

	@Autowired
	private AuthorDaoJpa authorDaoJpa;

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
	public void getByBook() {
		String bookTitle = "Book by Jack London";

		Collection<Author> actual = authorDaoJpa.getByBook(bookTitle);
		assertTrue(actual.size() == 1);
		assertEquals(actual.iterator().next().getName(), "Jack London");
	}

	@Test
	public void getByGenre() {
		String genreName = "Thriller";

		String authorName1 = "Steven King";
		String authorName2 = "Ambrose Bierce";

		Collection<Author> actual = authorDaoJpa.getByGenre(genreName);
		assertTrue(actual.size() == 2);
		assertNotNull(actual.stream().map(Author::getName).filter(name -> name.equals(authorName1)).findAny().orElse(null));
		assertNotNull(actual.stream().map(Author::getName).filter(name -> name.equals(authorName2)).findAny().orElse(null));
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
}