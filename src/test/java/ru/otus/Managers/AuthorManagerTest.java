package ru.otus.Managers;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.junit4.SpringRunner;
import ru.otus.Entities.Author;
import ru.otus.Exceptions.NotFoundException;
import ru.otus.Repositories.AuthorRepository;

import java.util.Collection;
import java.util.Collections;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@Ignore
@RunWith(SpringRunner.class)
public class AuthorManagerTest {
	@MockBean
	private AuthorRepository authorRepository;

	@Configuration
	static class AuthorManagerConfiguration {
		@Autowired
		private AuthorRepository authorRepository;

		@Bean
		public AuthorManager getAuthorManager() {
			return new AuthorManager(authorRepository);
		}
	}


	@Autowired
	private AuthorManager underTest;

	private String authorName = "AuthorDto";
	private Author expected;


	@Before
	public void setUp() {
		expected = new Author(authorName);
	}

	@Test
	public void createTest() {
		when(authorRepository.save(expected)).thenReturn(expected);
		Author actual = underTest.create(authorName);
		assertEquals(actual, expected);
	}

	@Test
	public void getByNameTest() {
		try {
			when(authorRepository.findByName(authorName)).thenReturn(expected);
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
			when(authorRepository.findByBook(title)).thenReturn(Collections.singleton(expected));
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
			when(authorRepository.findByGenre(genre)).thenReturn(Collections.singleton(expected));
			Collection<Author> actual = underTest.get("", genre, "");
			assertTrue(actual.contains(expected));
		} catch (NotFoundException e) {
			fail();
		}
	}

	@Test
	public void updateTest() {
		underTest.update(expected);
		verify(authorRepository).save(expected);
	}


	@Test
	public void deleteTest() {
		underTest.delete(expected);
		verify(authorRepository).delete(expected);
	}
}