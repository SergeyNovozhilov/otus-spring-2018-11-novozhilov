package ru.otus.Commands;

import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;
import ru.otus.Cache.Cache;
import ru.otus.Dao.AuthorDao;
import ru.otus.Dao.BookDao;
import ru.otus.Dao.GenreDao;
import ru.otus.Domain.Author;
import ru.otus.Domain.Book;
import ru.otus.Domain.Genre;

import java.util.*;

@ShellComponent
public class BookCRUD {

	private AuthorDao authorDao;
	private GenreDao genreDao;
	private BookDao bookDao;
	private Cache cache;

	public BookCRUD(AuthorDao authorDao, GenreDao genreDao, BookDao bookDao, Cache cache) {
		this.authorDao = authorDao;
		this.genreDao = genreDao;
		this.bookDao = bookDao;
		this.cache = cache;
	}

	@ShellMethod("Get Book cache")
	public void getBookCache() {
		List<Book> Books = (List<Book>)this.cache.get(Book.class);
		printBook(Books);
	}

	@ShellMethod("Create Book with title, genre and authors")
	public void createBook(String title, String genre, String author) {
		Genre genreObj = genreDao.getByName(genre);
		if (genreObj == null) {
			System.out.println("There is no Genre with name " + genre + ". Will be created. ");
			genreObj = new Genre(genre);
			genreDao.save(genreObj);
		}
		Book book = new Book(title, genreObj);
		String[] authors = author.split(",");
		if (genre != null && authors.length > 0) {
			// to be refactored to batch operation
			for (String a : authors) {
				Collection<Author> authorCollection = authorDao.getByName(a);
				if (authorCollection.isEmpty()) {
					System.out.println("There is no Author with name " + a + ". Pleases create it using create-author command.");
				} else {
					book.addAuthors(authorCollection);
				}
			}
		}
		bookDao.save(book);
		System.out.println("Book has been created: ");
		cache.add(Book.class, Collections.singletonList(book));
		printBook(Collections.singletonList(book));
	}

	@ShellMethod("Get Book by title and/or by genre and/or by author ")
	public void getBook(@ShellOption(defaultValue = "") String title, @ShellOption(defaultValue = "") String genre, @ShellOption(defaultValue = "") String author) {
		Collection<Book> books = new HashSet<>();
		Collection<Book> booksByTitle = null;
		Collection<Book> booksByGenre = null;
		Collection<Book> booksByAuthor = null;

		if (StringUtils.isBlank(title) && StringUtils.isBlank(genre) && StringUtils.isBlank(author)) {
			books.addAll(bookDao.getAll());
		} else {
			if (StringUtils.isNotBlank(title)) {
				booksByTitle = bookDao.getByTitle(title);
				if (!booksByTitle.isEmpty() && books.isEmpty()) {
					books.addAll(booksByTitle);
				}
			}

			if (StringUtils.isNotBlank(genre)) {
				booksByGenre = bookDao.getByGenre(genre);
				if (!booksByGenre.isEmpty() && books.isEmpty()) {
					books.addAll(booksByGenre);
				}
			}

			if (StringUtils.isNotBlank(author)) {
				booksByAuthor = bookDao.getByAuthor(author);
				if (!booksByAuthor.isEmpty() && books.isEmpty()) {
					books.addAll(booksByAuthor);
				}
			}

			if (booksByTitle != null) {
				books.retainAll(booksByTitle);
			}
			if (booksByGenre != null) {
				books.retainAll(booksByGenre);
			}
			if (booksByAuthor != null) {
				books.retainAll(booksByAuthor);
			}

			if (books.isEmpty()) {
				System.out.println("No Books were found.");
				return;
			}
		}

		List<Book> listBooks = new ArrayList<>(books);
		cache.add(Book.class, listBooks);
		printBook(listBooks);
	}

	@ShellMethod("Update Book by index")
	public void updateBook(int index, @ShellOption(defaultValue = "")String title, @ShellOption(defaultValue = "") String genre, @ShellOption(defaultValue = "")String author) {
		Book book = (Book)cache.get(Book.class, index);
		if (StringUtils.isNotBlank(title)) {
			book.setTitle(title);
		} else {
			return;
		}
		Genre genreObj = genreDao.getByName(genre);
		if (genreObj != null) {
			book.setGenre(genreObj);
		} else {
			System.out.println("There is no Genre with name " + genre);
		}

		if (StringUtils.isNotBlank(author)) {
			String[] authors = author.split(",");
			if (genre != null && authors.length > 0) {
				book.setAuthors(new HashSet<>());
				// to be refactored to batch operation
				for (String a : authors) {
					Collection<Author> authorCollection = authorDao.getByName(a);
					if (authorCollection.isEmpty()) {
						System.out.println("There is no Author with name " + a);
					} else {
						book.addAuthors(authorCollection);
					}
				}
			}
		}
		int res = bookDao.update(book);
		if (res > 0) {
			cache.add(Book.class, Collections.singletonList(book));
		} else {
			System.out.println("Cannot update Book with index: " + index);
		}
	}

	@ShellMethod("Delete Book by index")
	public void deleteBook(int index) {
		Book book = (Book)cache.get(Book.class, index);
		int res = bookDao.delete(book);
		if (res > 0) {
			cache.delete(Book.class, index);
		} else {
			System.out.println("Cannot delete Book with index: " + index);
		}
	}

	private void printBook(@NotNull List<Book> books) {
		for (int i = 0; i < books.size(); i ++) {
			Book book = books.get(i);
			System.out.println(i + ")");
			book.print();
		}
	}
}
