package ru.otus.Commands;

//import org.jetbrains.annotations.NotNull;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;
import ru.otus.Cache.Cache;
import ru.otus.Dtos.AuthorDto;
import ru.otus.Entities.Author;
import ru.otus.Exceptions.NotFoundException;
import ru.otus.Managers.AuthorManager;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

@ShellComponent
public class AuthorCRUD {

	private AuthorManager authorManager;
	private Cache cache;

	public AuthorCRUD(AuthorManager authorManager, Cache cache) {
		this.authorManager = authorManager;
		this.cache = cache;
	}

	@ShellMethod("Get AuthorDto cache")
	public void getAuthorCache() {
		List<AuthorDto> authors = (List<AuthorDto>)this.cache.get(AuthorDto.class);
		printAuthor(authors);
	}

	@ShellMethod("Create author with name")
	public void createAuthor(String name) {
		AuthorDto author = authorManager.create(name);
		cache.add(AuthorDto.class, Collections.singletonList(author));
		printAuthor(author);
	}

	@ShellMethod("Get author by name and/or by genre and/or by book ")
	public void getAuthor(@ShellOption(defaultValue = "") String name, @ShellOption(defaultValue = "") String genre, @ShellOption(defaultValue = "") String book) {
		try {
			Collection<AuthorDto> authors = authorManager.get(name, genre, book);
			cache.add(AuthorDto.class, new ArrayList<>(authors));
			printAuthor(authors);
		} catch (NotFoundException e) {
			System.out.println(e.getMessage());
		}
	}

	@ShellMethod("Update AuthorDto by index")
	public void updateAuthor(int index, @ShellOption(defaultValue = "")String name) {
		AuthorDto author = (AuthorDto)cache.get(AuthorDto.class, index);
		if (author != null) {
			author.setName(name);
			author = authorManager.update(author);
			cache.deleteAll(AuthorDto.class);
			cache.add(AuthorDto.class, Collections.singletonList(author));
			printAuthor(author);
		}
	}

	@ShellMethod("Delete AuthorDto by index")
	public void deleteAuthor(int index) {
		AuthorDto author = (AuthorDto)cache.get(AuthorDto.class, index);
		if (author != null) {
			authorManager.delete(author);
			cache.delete(AuthorDto.class, index);
		}
	}

	private void printAuthor(AuthorDto author) {
		printAuthor(Collections.singletonList(author));
	}

	private void printAuthor(Collection<AuthorDto> authors) {
		List<AuthorDto> array = new ArrayList<>(authors);
		for (int i = 0; i < array.size(); i ++) {
			AuthorDto author = array.get(i);
			System.out.println(i + ")");
			author.print();
		}
	}
}
