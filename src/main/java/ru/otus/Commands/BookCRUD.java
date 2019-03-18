package ru.otus.Commands;

import org.apache.commons.lang3.StringUtils;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;
import ru.otus.Cache.Cache;
import ru.otus.Dtos.BookDto;
import ru.otus.Entities.Book;
import ru.otus.Exceptions.NotFoundException;
import ru.otus.Managers.BookManager;

import java.util.*;

//import org.jetbrains.annotations.NotNull;

@ShellComponent
public class BookCRUD {

	private BookManager bookManager;
	private Cache cache;

	public BookCRUD(BookManager bookManager, Cache cache) {
		this.bookManager = bookManager;
		this.cache = cache;
	}

	@ShellMethod("Get BookDto cache")
	public void getBookCache() {
		List<BookDto> books = (List<BookDto>)this.cache.get(BookDto.class);
		printBook(books);
	}

	@ShellMethod("Create BookDto with title, genre and authors")
	public void createBook(String title, String genre, String author) {
		BookDto book = bookManager.create(title);
		if (book != null) {
			book = bookManager.addGenre(book, genre);

			String[] authors = author.split(",");

			if (authors != null && authors.length > 0) {
				book = bookManager.addAuthors(book, Arrays.asList(authors));
			}

			cache.add(BookDto.class, Collections.singletonList(book));
			printBook(book);
		}
	}

	@ShellMethod("Get BookDto by title and/or by genre and/or by author ")
	public void getBook(@ShellOption(defaultValue = "") String title, @ShellOption(defaultValue = "") String genre, @ShellOption(defaultValue = "") String author) {
		try {
			Collection<BookDto> books = bookManager.get(title, genre, author);
			cache.add(BookDto.class, new ArrayList<>(books));
			printBook(books);
		} catch (NotFoundException e) {
			System.out.println(e.getMessage());
		}
	}

	@ShellMethod("Update BookDto by index")
	public void updateBook(int index, @ShellOption(defaultValue = "")String title, @ShellOption(defaultValue = "") String genre, @ShellOption(defaultValue = "")String author) {
		BookDto book = (BookDto)cache.get(BookDto.class, index);
		if (book != null) {
			if (StringUtils.isNotBlank(title)) {
				book.setTitle(title);
				bookManager.update(book);
			} else {
				return;
			}
			book = bookManager.addGenre(book, genre);

			if (StringUtils.isNotBlank(author)) {
				String[] authors = author.split(",");
				if (authors != null && authors.length > 0) {
					book = bookManager.addAuthors(book, Arrays.asList(authors));
				}
			}

			cache.deleteAll(BookDto.class);
			cache.add(BookDto.class, Collections.singletonList(book));
			printBook(book);
		}
	}

	@ShellMethod("Add comment by book's index")
	public void addComment(int index, String comment) {
		BookDto book = (BookDto)cache.get(BookDto.class, index);
		if (book != null) {
			book = bookManager.addComment(book, comment);
			cache.delete(BookDto.class, index);
			cache.add(BookDto.class, Collections.singletonList(book));
			printBook(book);
		}
	}

	@ShellMethod("Remove comment by book's index")
	public void removeComment(int index, String comment) {
		BookDto book = (BookDto)cache.get(BookDto.class, index);
		if (book != null) {
			book = bookManager.removeComment(book, comment);
			cache.delete(BookDto.class, index);
			cache.add(BookDto.class, Collections.singletonList(book));
			printBook(book);
		}
	}

	@ShellMethod("Delete Book by index")
	public void deleteBook(int index) {
		BookDto book = (BookDto)cache.get(BookDto.class, index);
		bookManager.delete(book);
		cache.delete(Book.class, index);
	}

	private void printBook(/*@NotNull */ BookDto book) {
		printBook(Collections.singletonList(book));
	}

	private void printBook(/*@NotNull*/ Collection<BookDto> books) {
		List<BookDto> array = new ArrayList<>(books);
		for (int i = 0; i < array.size(); i ++) {
			BookDto book = array.get(i);
			System.out.println(i + ")");
			book.print();
		}
	}
}
