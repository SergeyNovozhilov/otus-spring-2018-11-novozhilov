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
import ru.otus.Domain.Genre;

import java.util.*;

@ShellComponent
public class GenreCRUD {
	private AuthorDao authorDao;
	private GenreDao genreDao;
	private BookDao bookDao;
	private Cache cache;

	public GenreCRUD(AuthorDao authorDao, GenreDao genreDao, BookDao bookDao, Cache cache) {
		this.authorDao = authorDao;
		this.genreDao = genreDao;
		this.bookDao = bookDao;
		this.cache = cache;
	}

	@ShellMethod("Get Genre cache")
	public void getGenreCache() {
		List<Genre> genres = (List<Genre>)this.cache.get(Genre.class);
		printGenre(genres);
	}

	@ShellMethod("Create genre")
	public void createGenre(String name) {
		Genre genreObj = new Genre(name);
		genreDao.save(genreObj);
		System.out.println("Create genre");
		cache.add(Genre.class, Collections.singletonList(genreObj));
		printGenre(genreObj);
	}

	@ShellMethod("Get genre by name or by author or by book")
	public void getGenre(@ShellOption(defaultValue = "") String name, @ShellOption(defaultValue = "") String author, @ShellOption(defaultValue = "") String book) {
		Collection<Genre> genres = new HashSet<>();
		if (StringUtils.isBlank(name) && StringUtils.isBlank(author) && StringUtils.isBlank(book)) {
			genres.addAll(genreDao.getAll());
		} else {
			if (StringUtils.isNotBlank(name)) {
				Genre genre = genreDao.getByName(name);
				if (genre != null) {
					cache.add(Genre.class, Collections.singletonList(genre));
					printGenre(Collections.singletonList(genre));
				} else {
					System.out.println("Genre with name " + name + " was not found");
				}
				return;
			}

			if (StringUtils.isNotBlank(book)) {
				Genre genre = genreDao.getByBook(book);
				if (genre != null) {
					cache.add(Genre.class, Collections.singletonList(genre));
					printGenre(Collections.singletonList(genre));
				} else {
					System.out.println("Genre of the book " + book + " was not found.");
				}
				return;
			}

			if (StringUtils.isNotBlank(author)) {
				genres.addAll(genreDao.getByAuthor(author));
				if (genres.isEmpty()) {
					System.out.println("Genre of author " + author + " was not found.");
				}
			}
		}

		cache.add(Genre.class, new ArrayList<>(genres));
		printGenre(genres);
	}

	@ShellMethod("Update Genre by index")
	public void updateGenre(int index, @ShellOption(defaultValue = "")String name) {
		Genre genre = (Genre) cache.get(Genre.class, index);
		if (StringUtils.isNotBlank(name)) {
			genre.setName(name);
			int res = genreDao.update(genre);
			if (res > 0) {
				cache.add(Genre.class, Collections.singletonList(genre));
				printGenre(genre);
			} else {
				System.out.println("Cannot update Genre with index: " + index);
			}
		}
	}


	@ShellMethod("Delete genre by index")
	public void deleteGenre(int index) {
		Genre genre = (Genre)cache.get(Genre.class, index);
		int res = genreDao.delete(genre);
		if (res > 0) {
			cache.delete(Genre.class, index);
		} else {
			System.out.println("Cannot delete Genre with index: " + index);
		}
	}

	private void printGenre(@NotNull Genre genre) {
		printGenre(Collections.singletonList(genre));
	}

	private void printGenre(@NotNull Collection<Genre> genres) {
		List<Genre> array = new ArrayList<>(genres);
		for (int i = 0; i < array.size(); i ++) {
			Genre genre = array.get(i);
			System.out.println(i + ")");
			genre.print();
		}
	}
}
