package ru.otus.Commands;

import org.apache.commons.lang3.StringUtils;
//import org.jetbrains.annotations.NotNull;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;
import ru.otus.Cache.Cache;
import ru.otus.Domain.Book;
import ru.otus.Exceptions.DataBaseException;
import ru.otus.Exceptions.NotFoundException;
import ru.otus.Managers.BookManager;

import java.util.*;

@ShellComponent
public class BookCRUD {

	private BookManager bookManager;
	private Cache cache;

	public BookCRUD(BookManager bookManager, Cache cache) {
		this.bookManager = bookManager;
		this.cache = cache;
	}

	@ShellMethod("Get Book cache")
	public void getBookCache() {
		List<Book> books = (List<Book>)this.cache.get(Book.class);
		printBook(books);
	}

	@ShellMethod("Create Book with title, genre and authors")
	public void createBook(String title, String genre, String author) {
		Book book = bookManager.create(title);
		bookManager.addGenre(book, genre);

		String[] authors = author.split(",");
		if (authors != null && authors.length > 0) {
			// to be refactored to batch operation
			for (String a : authors) {
				bookManager.addAuthor(book, a);
			}
		}

		try {
			bookManager.update(book);
			System.out.println("Book has been created.");
			cache.add(Book.class, Collections.singletonList(book));
			printBook(book);
		} catch (DataBaseException e) {
			System.out.println(e.getMessage());
		}
	}

	@ShellMethod("Get Book by title and/or by genre and/or by author ")
	public void getBook(@ShellOption(defaultValue = "") String title, @ShellOption(defaultValue = "") String genre, @ShellOption(defaultValue = "") String author) {
		try {
			Collection<Book> books = bookManager.get(title, genre, author);
			cache.add(Book.class, new ArrayList<>(books));
			printBook(books);
		} catch (NotFoundException e) {
			System.out.println(e.getMessage());
		}
	}

	@ShellMethod("Update Book by index")
	public void updateBook(int index, @ShellOption(defaultValue = "")String title, @ShellOption(defaultValue = "") String genre, @ShellOption(defaultValue = "")String author) {
		Book book = (Book)cache.get(Book.class, index);
		if (StringUtils.isNotBlank(title)) {
			book.setTitle(title);
		} else {
			return;
		}
		bookManager.addGenre(book, genre);

		if (StringUtils.isNotBlank(author)) {
			String[] authors = author.split(",");
			if (authors != null && authors.length > 0) {
				book.setAuthors(new HashSet<>());
				// to be refactored to batch operation
				for (String a : authors) {
					bookManager.addAuthor(book, a);
				}
			}
		}
		try {
			bookManager.update(book);
			cache.add(Book.class, Collections.singletonList(book));
			printBook(book);
		} catch (DataBaseException e) {
			System.out.println(e.getMessage());
		}
	}

	@ShellMethod("Delete Book by index")
	public void deleteBook(int index) {
		Book book = (Book)cache.get(Book.class, index);
		try {
			bookManager.delete(book);
			cache.delete(Book.class, index);
		} catch (DataBaseException e) {
			System.out.println(e.getMessage());
		}
	}

	private void printBook(/*@NotNull */ Book book) {
		printBook(Collections.singletonList(book));
	}

	private void printBook(/*@NotNull*/ Collection<Book> books) {
		List<Book> array = new ArrayList<>(books);
		for (int i = 0; i < array.size(); i ++) {
			Book book = array.get(i);
			System.out.println(i + ")");
			book.print();
		}
	}
}
