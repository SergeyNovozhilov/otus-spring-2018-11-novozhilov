package ru.otus.Commands;

import org.apache.commons.lang3.StringUtils;
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

	@ShellMethod
	public void getCache() {
		List<Author> authors = (List<Author>)this.cache.get(Author.class);
		printAuthor(authors);
	}

	@ShellMethod("Create author")
	public void createAuthor(String name, @ShellOption String genre) {
		Author author = new Author(name);
		String[] genres = genre.split(" ");
		if (genre != null && genres.length > 0) {
			for (String g : genres) {
				Genre genreObj = genreDao.getByName(g);
				if (genreObj == null) {
					System.out.println("There is no Genre with name " + g + ". Will be created. ");
					genreObj = new Genre(g);
				}
				author.addGenre(genreObj);
			}
		}
		authorDao.save(author);
		System.out.println("Author has been created: ");
		cache.add(Author.class, Collections.singletonList(author));
		printAuthor(Collections.singletonList(author));
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

		List<Author> listAuthors = new ArrayList<>(authors);
		cache.add(Author.class, listAuthors);
		printAuthor(listAuthors);
	}

	@ShellMethod
	public void updateAuthor(String name, @ShellOption String genre) {
		// TBD
	}

	@ShellMethod
	public void deleteAuthor(int index) {
		Author author = (Author)cache.get(Author.class, index);
		int res = authorDao.delete(author);
		if (res > 0) {
			cache.delete(Author.class, index);
		} else {
			System.out.println("Cannot delete Author with index: " + index);
		}
	}

	private void printAuthor(List<Author> authors) {
		for (int i = 0; i < authors.size(); i ++) {
			Author author = authors.get(i);
			System.out.println(i + ") Name: " + author.getName());
			System.out.println("Genres:");
			if (author.getGenres() != null) {
				for (Genre genre : author.getGenres()) {
					System.out.println("   " + genre.getName());
				}
			}
		}
	}
}
