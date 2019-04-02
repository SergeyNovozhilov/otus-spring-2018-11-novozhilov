package ru.otus.commands;

import org.apache.commons.lang3.StringUtils;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.cache.Cache;
import ru.otus.entities.Book;
import ru.otus.exceptions.NotFoundException;
import ru.otus.managers.BookManager;
import ru.otus.services.EntityPrinter;

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
		EntityPrinter.print(Collections.unmodifiableList(this.cache.get(Book.class)));
	}

	@ShellMethod("Create Book with title, genre and authors")
	public void createBook(String title, String genre, String author) {
		Book book = bookManager.create(title);
		if (book != null) {
			book = bookManager.addGenre(book, genre);

			String[] authors = author.split(",");

			if (authors != null && authors.length > 0) {
				book = bookManager.addAuthors(book, Arrays.asList(authors));
			}

			cache.add(Book.class, Collections.singletonList(book));
			EntityPrinter.print(book);
		}
	}

	@ShellMethod("Get Book by title and/or by genre and/or by author ")
	@Transactional
	public void getBook(@ShellOption(defaultValue = "") String title, @ShellOption(defaultValue = "") String genre, @ShellOption(defaultValue = "") String author) {
		try {
			Collection<Book> books = bookManager.get(title, genre, author);
			cache.add(Book.class, new ArrayList<>(books));
			EntityPrinter.print(Collections.unmodifiableCollection(books));
		} catch (NotFoundException e) {
			System.out.println(e.getMessage());
		}
	}

	@ShellMethod("Update Book by index")
	public void updateBook(int index, @ShellOption(defaultValue = "")String title, @ShellOption(defaultValue = "") String genre, @ShellOption(defaultValue = "")String author) {
		Book book = (Book)cache.get(Book.class, index);
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

			cache.deleteAll(Book.class);
			cache.add(Book.class, Collections.singletonList(book));

			EntityPrinter.print(book);
		}
	}

	@ShellMethod("Add comment by book's index")
	public void addComment(int index, String comment) {
		Book book = (Book)cache.get(Book.class, index);
		if (book != null) {
			book = bookManager.addComment(book, comment);
			cache.delete(Book.class, index);
			cache.add(Book.class, Collections.singletonList(book));
			EntityPrinter.print(book);
		}
	}

	@ShellMethod("Remove comment by book's index")
	public void removeComment(int index, String comment) {
		Book book = (Book)cache.get(Book.class, index);
		if (book != null) {
			book = bookManager.removeComment(book, comment);
			cache.delete(Book.class, index);
			cache.add(Book.class, Collections.singletonList(book));
			EntityPrinter.print(book);
		}
	}

	@ShellMethod("Delete Book by index")
	public void deleteBook(int index) {
		Book book = (Book)cache.get(Book.class, index);
		bookManager.delete(book);
		cache.delete(Book.class, index);
	}
}
