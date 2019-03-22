package ru.otus.Commands;

//import org.jetbrains.annotations.NotNull;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;
import ru.otus.Cache.Cache;
import ru.otus.Entities.Genre;
import ru.otus.Exceptions.NotFoundException;
import ru.otus.Managers.GenreManager;

import java.util.*;

@ShellComponent
public class GenreCRUD {
	private GenreManager genreManager;
	private Cache cache;

	public GenreCRUD(GenreManager genreManager, Cache cache) {
		this.genreManager = genreManager;
		this.cache = cache;
	}

	@ShellMethod("Get Genre cache")
	public void getGenreCache() {
		List<Genre> genres = (List<Genre>)this.cache.get(Genre.class);
		printGenre(genres);
	}

	@ShellMethod("Create genre")
	public void createGenre(String name) {
	    Genre genre = genreManager.create(name);
	    cache.add(Genre.class, Collections.singletonList(genre));
	    printGenre(genre);
	}

	@ShellMethod("Get genre by name or by author or by book")
	public void getGenre(@ShellOption(defaultValue = "") String name, @ShellOption(defaultValue = "") String author, @ShellOption(defaultValue = "") String book) {
        try {
            Collection<Genre> genres = genreManager.get(name, book, author);
            cache.add(Genre.class, new ArrayList<>(genres));
            printGenre(genres);
        } catch (NotFoundException e) {
            System.out.println(e.getMessage());
        }
    }

	@ShellMethod("Update Genre by index")
	public void updateGenre(int index, @ShellOption(defaultValue = "")String name) {
		Genre genre = (Genre) cache.get(Genre.class, index);
		if (genre != null) {
			genre.setName(name);
			genreManager.update(genre);
			cache.deleteAll(Genre.class);
			cache.add(Genre.class, Collections.singletonList(genre));
			printGenre(genre);
		}
    }


	@ShellMethod("Delete genre by index")
	public void deleteGenre(int index) {
		Genre genre = (Genre)cache.get(Genre.class, index);
        if (genre != null) {
			genreManager.delete(genre);
			cache.delete(Genre.class, index);
		}
	}

	private void printGenre(Genre genre) {
		printGenre(Collections.singletonList(genre));
	}

	private void printGenre(Collection<Genre> genres) {
		List<Genre> array = new ArrayList<>(genres);
		for (int i = 0; i < array.size(); i ++) {
			Genre genre = array.get(i);
			System.out.println(i + ")");
			genre.print();
		}
	}
}
