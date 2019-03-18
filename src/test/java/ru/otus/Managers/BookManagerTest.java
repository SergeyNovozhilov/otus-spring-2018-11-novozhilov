package ru.otus.Managers;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.annotation.Id;
import org.springframework.test.context.junit4.SpringRunner;
import ru.otus.Dtos.BookDto;
import ru.otus.Entities.Book;
import ru.otus.Exceptions.NotFoundException;
import ru.otus.Repositories.AuthorRepository;
import ru.otus.Repositories.BookRepository;
import ru.otus.Repositories.GenreRepository;

import java.util.Collection;
import java.util.Collections;

import static org.junit.Assert.*;
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
		@Autowired
		private ModelMapper modelMapper;

		@Bean
		public BookManager getBookManager() {
			return new BookManager(authorRepository, genreRepository, bookRepository, modelMapper);
		}
	}


	@Autowired
	private BookManager underTest;

	private String book = "BookDto";
	private Book expected;


	@Before
	public void setUp() {
		expected = new Book(book);
	}

	@Test
	public void createTest() {
		when(bookRepository.save(expected)).thenReturn(expected);
		BookDto actual = underTest.create(book);
		assertEquals(expected, actual);
	}

	@Test
	public void getByTitleTest() {
		try {
			when(bookRepository.findByTitle(book)).thenReturn(Collections.singleton(expected));
			Collection<BookDto> actual = underTest.get(book, "", "");
			assertTrue(actual.contains(expected));
		} catch (NotFoundException e) {
			fail();
		}
	}

	@Test
	public void getByAuthorTest() {
		String name = "AuthorDto";
		try {
			when(bookRepository.findByAuthor(name)).thenReturn(Collections.singleton(expected));
			Collection<BookDto> actual = underTest.get("", "", name);
			assertTrue(actual.contains(expected));
		} catch (NotFoundException e) {
			fail();
		}
	}

	@Test
	public void getByGenreTest() {
		String genre = "GenreDto";
		try {
			when(bookRepository.findByGenre(genre)).thenReturn(Collections.singleton(expected));
			Collection<BookDto> actual = underTest.get("", genre, "");
			assertTrue(actual.contains(expected));
		} catch (NotFoundException e) {
			fail();
		}
	}

	@Test
	public void updateTest() {
//		underTest.update(expected);
//		verify(bookRepository).save(expected);
	}


	@Test
	public void deleteTest() {
//		underTest.delete(expected);
//		verify(bookRepository).delete(expected);
	}
}