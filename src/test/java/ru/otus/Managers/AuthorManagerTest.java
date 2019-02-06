package ru.otus.Managers;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.junit4.SpringRunner;
import ru.otus.Dao.AuthorDao;
import ru.otus.Domain.Author;
import ru.otus.Exceptions.DataBaseException;
import ru.otus.Exceptions.NotFoundException;

import java.util.Collection;
import java.util.Collections;

import static org.junit.Assert.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
public class AuthorManagerTest {
	@MockBean
	private AuthorDao authorDao;

	@Configuration
	static class AuthorManagerConfiguration {
		@Autowired
		private AuthorDao authorDao;

		@Bean
		public AuthorManager getAuthorManager() {
			return new AuthorManager(authorDao);
		}
	}


	@Autowired
	private AuthorManager underTest;

	private String authorName = "Author";
	private Author expected;


	@Before
	public void setUp() {
		expected = new Author(authorName);
	}

	@Test
	public void createTest() {
		Author actual = underTest.create(authorName);
		assertEquals(actual, expected);
	}

	@Test
	public void getByNameTest() {
		try {
			when(authorDao.getByName(authorName)).thenReturn(expected);
			Collection<Author> actual = underTest.get(authorName, "", "");
			assertTrue(actual.contains(expected));
		} catch (NotFoundException e) {
			fail();
		}
	}

	@Test
	public void getByBookTest() {
		String title = "Book";
		try {
			when(authorDao.getByBook(title)).thenReturn(Collections.singleton(expected));
			Collection<Author> actual = underTest.get("", "", title);
			assertTrue(actual.contains(expected));
		} catch (NotFoundException e) {
			fail();
		}
	}

	@Test
	public void getByGenreTest() {
		String genre = "Genre";
		try {
			when(authorDao.getByGenre(genre)).thenReturn(Collections.singleton(expected));
			Collection<Author> actual = underTest.get("", genre, "");
			assertTrue(actual.contains(expected));
		} catch (NotFoundException e) {
			fail();
		}
	}

	@Test
	public void updateTest() {
		underTest.update(expected);
		verify(authorDao).update(expected);
	}


	@Test
	public void deleteTest() {
		underTest.delete(expected);
		verify(authorDao).delete(expected);
	}
}