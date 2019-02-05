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
import ru.otus.Dao.BookDao;
import ru.otus.Dao.GenreDao;
import ru.otus.Domain.Book;
import ru.otus.Exceptions.DataBaseException;
import ru.otus.Exceptions.NotFoundException;

import java.util.Collection;
import java.util.Collections;

import static org.junit.Assert.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
public class BookManagerTest {
	@MockBean
	private BookDao bookDao;
	@MockBean
	private AuthorDao authorDao;
	@MockBean
	private GenreDao genreDao;

	@Configuration
	static class BookManagerConfiguration {
		@Autowired
		private BookDao bookDao;
		@Autowired
		private AuthorDao authorDao;
		@Autowired
		private GenreDao genreDao;

		@Bean
		public BookManager getBookManager() {
			return new BookManager(authorDao, genreDao, bookDao);
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
		when(bookDao.save(expected)).thenReturn(expected);
		Book actual = underTest.create(book);
		assertEquals(expected, actual);
	}

	@Test
	public void getByTitleTest() {
		try {
			when(bookDao.getByTitle(book)).thenReturn(Collections.singleton(expected));
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
			when(bookDao.getByAuthor(name)).thenReturn(Collections.singleton(expected));
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
			when(bookDao.getByGenre(genre)).thenReturn(Collections.singleton(expected));
			Collection<Book> actual = underTest.get("", genre, "");
			assertTrue(actual.contains(expected));
		} catch (NotFoundException e) {
			fail();
		}
	}

	@Test
	public void updateTest() {
		try {
			underTest.update(expected);
			verify(bookDao).update(expected);
		} catch (DataBaseException e) {
			fail();
		}
	}


	@Test
	public void deleteTest() {
		try {
			underTest.delete(expected);
			verify(bookDao).delete(expected);
		} catch (DataBaseException e) {
			fail();
		}
	}
}