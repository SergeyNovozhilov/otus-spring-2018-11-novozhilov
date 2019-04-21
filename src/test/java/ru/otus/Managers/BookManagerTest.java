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
import ru.otus.entities.Book;
import ru.otus.Exceptions.NotFoundException;
import ru.otus.Repositories.BookRepository;

import java.util.Collection;
import java.util.Collections;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@Ignore
@RunWith(SpringRunner.class)
public class BookManagerTest {
	@MockBean
	private BookRepository bookRepository;
	@MockBean
	private AuthorRepository authorRepository;
	@MockBean
	private GenreRepository genreRepository;

	@Configuration
	static class BookManagerConfiguration {
		@Autowired
		private BookRepository bookRepository;
		@Autowired
		private AuthorRepository authorRepository;
		@Autowired
		private GenreRepository genreRepository;

		@Bean
		public BookManager getBookManager() {
			return new BookManager(authorRepository, genreRepository, bookRepository);
		}
	}


	@Autowired
	private BookManager underTest;

	private String book = "Book";
	private Book expected;


	@Before
	public void setUp() {
		expected = new Book(book);
	}

	@Test
	public void createTest() {
		when(bookRepository.save(expected)).thenReturn(expected);
		Book actual = underTest.create(book);
		assertEquals(expected, actual);
	}

	@Test
	public void getByTitleTest() {
		try {
			when(bookRepository.findByTitle(book)).thenReturn(Collections.singleton(expected));
			Collection<Book> actual = underTest.get(book, "", "");
			assertTrue(actual.contains(expected));
		} catch (NotFoundException e) {
			fail();
		}
	}

	@Test
	public void getByAuthorTest() {
		String name = "Author";
		try {
			when(bookRepository.findByAuthor(name)).thenReturn(Collections.singleton(expected));
			Collection<Book> actual = underTest.get("", "", name);
			assertTrue(actual.contains(expected));
		} catch (NotFoundException e) {
			fail();
		}
	}

	@Test
	public void getByGenreTest() {
		String genre = "Genre";
		try {
			when(bookRepository.findByGenre(genre)).thenReturn(Collections.singleton(expected));
			Collection<Book> actual = underTest.get("", genre, "");
			assertTrue(actual.contains(expected));
		} catch (NotFoundException e) {
			fail();
		}
	}

	@Test
	public void updateTest() {
		underTest.update(expected);
		verify(bookRepository).save(expected);
	}


	@Test
	public void deleteTest() {
		underTest.delete(expected);
		verify(bookRepository).delete(expected);
	}
}