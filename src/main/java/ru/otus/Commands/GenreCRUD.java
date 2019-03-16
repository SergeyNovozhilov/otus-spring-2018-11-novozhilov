package ru.otus.Commands;

//import org.jetbrains.annotations.NotNull;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;
import ru.otus.Cache.Cache;
import ru.otus.Dtos.GenreDto;
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

	@ShellMethod("Get GenreDto cache")
	public void getGenreCache() {
		List<GenreDto> genres = (List<GenreDto>)this.cache.get(GenreDto.class);
		printGenre(genres);
	}

	@ShellMethod("Create genre")
	public void createGenre(String name) {
	    GenreDto genre = genreManager.create(name);
	    cache.add(GenreDto.class, Collections.singletonList(genre));
	    printGenre(genre);
	}

	@ShellMethod("Get genre by name or by author or by book")
	public void getGenre(@ShellOption(defaultValue = "") String name, @ShellOption(defaultValue = "") String author, @ShellOption(defaultValue = "") String book) {
        try {
            Collection<GenreDto> genres = genreManager.get(name, book, author);
            cache.add(GenreDto.class, new ArrayList<>(genres));
            printGenre(genres);
        } catch (NotFoundException e) {
            System.out.println(e.getMessage());
        }
    }

	@ShellMethod("Update GenreDto by index")
	public void updateGenre(int index, @ShellOption(defaultValue = "")String name) {
		GenreDto genre = (GenreDto) cache.get(GenreDto.class, index);
		if (genre != null) {
			genre.setName(name);
			genreManager.update(genre);
			cache.deleteAll(GenreDto.class);
			cache.add(GenreDto.class, Collections.singletonList(genre));
			printGenre(genre);
		}
    }


	@ShellMethod("Delete genre by index")
	public void deleteGenre(int index) {
		GenreDto genre = (GenreDto)cache.get(GenreDto.class, index);
        if (genre != null) {
			genreManager.delete(genre);
			cache.delete(GenreDto.class, index);
		}
	}

	private void printGenre(GenreDto genre) {
		printGenre(Collections.singletonList(genre));
	}

	private void printGenre(Collection<GenreDto> genres) {
		List<GenreDto> array = new ArrayList<>(genres);
		for (int i = 0; i < array.size(); i ++) {
			GenreDto genre = array.get(i);
			System.out.println(i + ")");
			genre.print();
		}
	}
}
