package ru.otus.commands;

import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;
import ru.otus.cache.Cache;
import ru.otus.entities.Genre;
import ru.otus.exceptions.NotFoundException;
import ru.otus.managers.GenreManager;
import ru.otus.services.EntityPrinter;

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
		EntityPrinter.print(Collections.unmodifiableList(this.cache.get(Genre.class)));
	}

	@ShellMethod("Create genre")
	public void createGenre(String name) {
	    Genre genre = genreManager.create(name);
	    cache.add(Genre.class, Collections.singletonList(genre));
		EntityPrinter.print(genre);
	}

	@ShellMethod("Get genre by name or by author or by book")
	public void getGenre(@ShellOption(defaultValue = "") String name, @ShellOption(defaultValue = "") String author, @ShellOption(defaultValue = "") String book) {
        try {
            Collection<Genre> genres = genreManager.get(name, book, author);
            cache.add(Genre.class, new ArrayList<>(genres));
			  EntityPrinter.print(Collections.unmodifiableCollection(genres));
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
			EntityPrinter.print(genre);
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
}
