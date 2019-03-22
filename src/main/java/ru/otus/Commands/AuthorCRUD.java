package ru.otus.Commands;

//import org.jetbrains.annotations.NotNull;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;
import ru.otus.Cache.Cache;
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

	@ShellMethod("Get Author cache")
	public void getAuthorCache() {
		List<Author> authors = (List<Author>)this.cache.get(Author.class);
		printAuthor(authors);
	}

	@ShellMethod("Create author with name")
	public void createAuthor(String name) {
		Author author = authorManager.create(name);
		cache.add(Author.class, Collections.singletonList(author));
		printAuthor(author);
	}

	@ShellMethod("Get author by name and/or by genre and/or by book ")
	public void getAuthor(@ShellOption(defaultValue = "") String name, @ShellOption(defaultValue = "") String genre, @ShellOption(defaultValue = "") String book) {
		try {
			Collection<Author> authors = authorManager.get(name, genre, book);
			cache.add(Author.class, new ArrayList<>(authors));
			printAuthor(authors);
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
			printAuthor(author);
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

	private void printAuthor(Author author) {
		printAuthor(Collections.singletonList(author));
	}

	private void printAuthor(Collection<Author> authors) {
		List<Author> array = new ArrayList<>(authors);
		for (int i = 0; i < array.size(); i ++) {
			Author author = array.get(i);
			System.out.println(i + ")");
			author.print();
		}
	}
}
