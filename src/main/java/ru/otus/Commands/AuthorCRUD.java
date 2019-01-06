package ru.otus.Commands;

import org.apache.commons.lang3.StringUtils;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;
import ru.otus.Dao.AuthorDao;
import ru.otus.Dao.BookDao;
import ru.otus.Dao.GenreDao;
import ru.otus.Domain.Author;
import ru.otus.Domain.Genre;

import java.util.Collection;
import java.util.HashSet;

@ShellComponent
public class AuthorCRUD {

	private AuthorDao authorDao;
	private GenreDao genreDao;
	private BookDao bookDao;

	public AuthorCRUD(AuthorDao authorDao, GenreDao genreDao, BookDao bookDao) {
		this.authorDao = authorDao;
		this.genreDao = genreDao;
		this.bookDao = bookDao;
	}

	@ShellMethod("Create author")
	public void createAuthor(String name, @ShellOption String[] genre) {
		Author author = new Author(name);
		if (genre != null && genre.length > 0) {
			for (String g : genre) {
				Genre genreObj = genreDao.getByName(g);
				if (genreObj == null) {
					System.out.println("There is no Genre with name " + g);
				} else {
					author.addGenre(genreObj);
				}
			}
		}
		authorDao.save(author);
		System.out.println("Author with name: " + name + " and genres: " + genre.toString() + " has been created.");
	}

	@ShellMethod("Get author")
	public void getAuthor(@ShellOption String name, @ShellOption String genre, @ShellOption String book) {
		Collection<Author> authors = new HashSet<>();
		Collection<Author> authorsByName = new HashSet<>();
		Collection<Author> authorsByGenre = new HashSet<>();
		Collection<Author> authorsByBook = new HashSet<>();

		if (StringUtils.isNotBlank(name)) {
			authorsByName = authorDao.getByName(name);
			if (!authorsByName.isEmpty() && authors.isEmpty()) {
				authors.addAll(authorsByName);
			}
		}

		if (StringUtils.isNotBlank(genre)) {
			authorsByGenre = authorDao.getByGenre(genre);
			if (!authorsByGenre.isEmpty() && authors.isEmpty()) {
				authors.addAll(authorsByGenre);
			}
		}

		if (StringUtils.isNotBlank(book)) {
			authorsByBook = authorDao.getByBook(book);
			if (!authorsByBook.isEmpty() && authors.isEmpty()) {
				authors.addAll(authorsByBook);
			}
		}

		if (authors.isEmpty()) {
			System.out.println("No authors were found.");
			return;
		}

		authors.retainAll(authorsByName);
		authors.retainAll(authorsByGenre);
		authors.retainAll(authorsByBook);

		authors.forEach(this::printAuthor);
	}

	private void printAuthor(Author author) {
		System.out.println("Name: " + author.getName());
		System.out.println("Genres:");
		if (author.getGenres() != null) {
			for (Genre genre : author.getGenres()) {
				System.out.println("   " + genre.getName());
			}
		}
	}
}
