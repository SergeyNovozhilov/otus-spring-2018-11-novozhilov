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
import ru.otus.entities.Genre;
import ru.otus.Exceptions.NotFoundException;

import java.util.Collection;
import java.util.Collections;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@Ignore
@RunWith(SpringRunner.class)
public class GenreManagerTest {
	@MockBean
	private GenreRepository genreRepository;

	@Configuration
	static class GenreManagerConfiguration {
		@Autowired
		private GenreRepository genreRepository;

		@Bean
		public GenreManager getGenreManager() {
			return new GenreManager(genreRepository);
		}
	}


	@Autowired
	private GenreManager underTest;

	private String genreName = "GenreDto";
	private Genre expected;


	@Before
	public void setUp() {
		expected = new Genre(genreName);
	}

	@Test
	public void createTest() {
		Genre actual = underTest.create(genreName);
		assertEquals(actual, expected);
	}

	@Test
	public void getByNameTest() {
		try {
			when(genreRepository.findByName(genreName)).thenReturn(expected);
			Collection<Genre> actual = underTest.get(genreName, "", "");
			assertTrue(actual.contains(expected));
		} catch (NotFoundException e) {
			fail();
		}
	}

	@Test
	public void getByBookTest() {
		String title = "Book";
		try {
			when(genreRepository.findByBook(title)).thenReturn(expected);
			Collection<Genre> actual = underTest.get("", title, "");
			assertTrue(actual.contains(expected));
		} catch (NotFoundException e) {
			fail();
		}
	}

	@Test
	public void getByAuthorTest() {
		String author = "Author";
		try {
			when(genreRepository.findByAuthor(author)).thenReturn(Collections.singleton(expected));
			Collection<Genre> actual = underTest.get("", "", author);
			assertTrue(actual.contains(expected));
		} catch (NotFoundException e) {
			fail();
		}
	}

	@Test
	public void updateTest() {
		underTest.update(expected);
		verify(genreRepository).save(expected);
	}


	@Test
	public void deleteTest() {
		underTest.delete(expected);
		verify(genreRepository).delete(expected);
	}
}