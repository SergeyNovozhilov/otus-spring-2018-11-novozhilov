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
import ru.otus.Domain.Genre;

import java.util.*;

@ShellComponent
public class AuthorCRUD {

	private AuthorDao authorDao;
	private GenreDao genreDao;
	private BookDao bookDao;
	private Cache cache;

	public AuthorCRUD(AuthorDao authorDao, GenreDao genreDao, BookDao bookDao, Cache cache) {
		this.authorDao = authorDao;
		this.genreDao = genreDao;
		this.bookDao = bookDao;
		this.cache = cache;
	}

	@ShellMethod("Get Author cache")
	public void getAuthorCache() {
		List<Author> authors = (List<Author>)this.cache.get(Author.class);
		printAuthor(authors);
	}

	@ShellMethod("Create author with name and genre")
	public void createAuthor(String name, @ShellOption String genre) {
		Author author = new Author(name);
		String[] genres = genre.split(",");
		if (genre != null && genres.length > 0) {
			// to be refactored to batch operation
			for (String g : genres) {
				Genre genreObj = genreDao.getByName(g);
				if (genreObj == null) {
					System.out.println("There is no Genre with name " + g + ". Will be created. ");
					genreObj = new Genre(g);
					genreDao.save(genreObj);
				}
				author.addGenre(genreObj);
			}
		}
		authorDao.save(author);
		System.out.println("Author has been created: ");
		cache.add(Author.class, Collections.singletonList(author));
		printAuthor(Collections.singletonList(author));
	}

	@ShellMethod("Get author by name and/or by genre and/or by book ")
	public void getAuthor(@ShellOption(defaultValue = "") String name, @ShellOption(defaultValue = "") String genre, @ShellOption(defaultValue = "") String book) {
		Collection<Author> authors = new HashSet<>();
		Collection<Author> authorsByName = null;
		Collection<Author> authorsByGenre = null;
		Collection<Author> authorsByBook = null;

		if (StringUtils.isBlank(name) && StringUtils.isBlank(genre) && StringUtils.isBlank(book)) {
			authors.addAll(authorDao.getAll());
		} else {
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

			if (authorsByName != null) {
				authors.retainAll(authorsByName);
			}
			if (authorsByGenre != null) {
				authors.retainAll(authorsByGenre);
			}
			if (authorsByBook != null) {
				authors.retainAll(authorsByBook);
			}

			if (authors.isEmpty()) {
				System.out.println("No authors were found.");
				return;
			}
		}
		List<Author> listAuthors = new ArrayList<>(authors);
		cache.add(Author.class, listAuthors);
		printAuthor(listAuthors);
	}

	@ShellMethod("Update Author by index")
	public void updateAuthor(int index, @ShellOption(defaultValue = "")String name, @ShellOption(defaultValue = "") String genre) {
		Author author = (Author)cache.get(Author.class, index);
		if (StringUtils.isNotBlank(name)) {
			author.setName(name);
		}

		if (StringUtils.isNotBlank(genre)) {
			String[] genres = genre.split(",");
			if (genre != null && genres.length > 0) {
				author.setGenres(new HashSet<>());
				// to be refactored to batch operation
				for (String g : genres) {
					Genre genreObj = genreDao.getByName(g);
					if (genreObj == null) {
						System.out.println("There is no Genre with name " + g + ". Will be created. ");
						genreObj = new Genre(g);
						genreDao.save(genreObj);
					}
					author.addGenre(genreObj);
				}
			}
		}
		int res = authorDao.update(author);
		if (res > 0) {
			cache.add(Author.class, Collections.singletonList(author));
		} else {
			System.out.println("Cannot update Author with index: " + index);
		}
	}

	@ShellMethod("Delete Author by index")
	public void deleteAuthor(int index) {
		Author author = (Author)cache.get(Author.class, index);
		int res = authorDao.delete(author);
		if (res > 0) {
			cache.delete(Author.class, index);
		} else {
			System.out.println("Cannot delete Author with index: " + index);
		}
	}

	private void printAuthor(@NotNull List<Author> authors) {
		for (int i = 0; i < authors.size(); i ++) {
			Author author = authors.get(i);
			System.out.println(i + ")");
			author.print();
		}
	}
}
