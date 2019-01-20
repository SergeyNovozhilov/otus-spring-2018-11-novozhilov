package ru.otus.IntegrationTests;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit4.SpringRunner;
import ru.otus.DaoImpl.GenreDaoJpa;
import ru.otus.Domain.Genre;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

@RunWith(SpringRunner.class)
@DataJpaTest
@Import(GenreDaoJpa.class)
public class GenreDaoJpaTest {

	@Autowired
	private TestEntityManager testEntityManager;

	@Autowired
	private GenreDaoJpa genreDaoJpa;

	@Test
	public void getAll() {
		List<Genre> expected = Arrays.asList(new Genre("Novel"), new Genre("Humor"));
		expected.forEach(e -> testEntityManager.persist(e));
		testEntityManager.flush();
		Collection<Genre> actual = genreDaoJpa.getAll();
		assertEquals(actual, expected);
	}

	@Test
	public void getByName() {
		Genre expected = new Genre("Novel");
		testEntityManager.persistAndFlush(expected);
		Genre actual = genreDaoJpa.getByName(expected.getName());
		assertEquals(actual, expected);
	}

	@Test
	public void getById() {
		Genre expected = new Genre("Novel");
		testEntityManager.persistAndFlush(expected);
		Genre actual = genreDaoJpa.getById(expected.getId());
		assertEquals(actual, expected);
	}

	@Test
	public void getByAuthor() {
	}

	@Test
	public void getByBook() {
		String expectedGenreName = "Novel";
		String book = "Book by Jack London";
		Genre actual = genreDaoJpa.getByBook(book);
		assertEquals(actual.getName(), expectedGenreName);
	}

	@Test
	public void save() {
		Genre expected = new Genre("Humor");
		genreDaoJpa.save(expected);
		Genre actual = testEntityManager.find(Genre.class, expected.getId());
		assertEquals(actual, expected);
	}

	@Test
	public void delete() {
		Genre expected = new Genre("Novel");
		testEntityManager.persistAndFlush(expected);
		Genre actual = genreDaoJpa.getById(expected.getId());
		assertEquals(actual, expected);
		genreDaoJpa.delete(expected);
		actual = testEntityManager.find(Genre.class, expected.getId());
		assertNull(actual);
	}

	@Test
	public void update() {
		String newName = "Romantic";
		Genre expected = new Genre("Novel");
		genreDaoJpa.save(expected);
		Genre actual = testEntityManager.find(Genre.class, expected.getId());
		assertEquals(actual, expected);
		expected.setName(newName);
		genreDaoJpa.update(expected);
		actual = testEntityManager.find(Genre.class, expected.getId());
		assertEquals(actual.getName(), expected.getName());
	}
}