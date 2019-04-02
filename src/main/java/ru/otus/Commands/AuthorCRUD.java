package ru.otus.commands;

import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.cache.Cache;
import ru.otus.entities.Author;
import ru.otus.exceptions.NotFoundException;
import ru.otus.managers.AuthorManager;
import ru.otus.services.EntityPrinter;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

@ShellComponent
public class AuthorCRUD {

	private AuthorManager authorManager;
	private Cache cache;

	public AuthorCRUD(AuthorManager authorManager, Cache cache) {
		this.authorManager = authorManager;
		this.cache = cache;
	}

	@ShellMethod("Get Author cache")
	public void getAuthorCache() {
		EntityPrinter.print(Collections.unmodifiableList(this.cache.get(Author.class)));
	}

	@ShellMethod("Create author with name")
	public void createAuthor(String name) {
		Author author = authorManager.create(name);
		cache.add(Author.class, Collections.singletonList(author));
		EntityPrinter.print(author);
	}

	@ShellMethod("Get author by name and/or by genre and/or by book ")
	@Transactional
	public void getAuthor(@ShellOption(defaultValue = "") String name, @ShellOption(defaultValue = "") String genre, @ShellOption(defaultValue = "") String book) {
		try {
			Collection<Author> authors = authorManager.get(name, genre, book);
			cache.add(Author.class, new ArrayList<>(authors));
			EntityPrinter.print(Collections.unmodifiableCollection(authors));
		} catch (NotFoundException e) {
			System.out.println(e.getMessage());
		}
	}

	@ShellMethod("Update Author by index")
	public void updateAuthor(int index, @ShellOption(defaultValue = "")String name) {
		Author author = (Author)cache.get(Author.class, index);
		if (author != null) {
			author.setName(name);
			author = authorManager.update(author);
			cache.deleteAll(Author.class);
			cache.add(Author.class, Collections.singletonList(author));
			EntityPrinter.print(author);
		}
	}

	@ShellMethod("Delete Author by index")
	public void deleteAuthor(int index) {
		Author author = (Author)cache.get(Author.class, index);
		if (author != null) {
			authorManager.delete(author);
			cache.delete(Author.class, index);
		}
	}
}
