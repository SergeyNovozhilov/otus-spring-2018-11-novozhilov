package ru.otus.entities;

import lombok.Data;

import java.util.Collection;
import java.util.Objects;
import java.util.UUID;

@Data
public class Author implements IEntity{
	private String id;
	private String name;
	private Collection<Book> books;

	public Author() {
	}

	public Author(String name) {
		this.name = name;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Collection<Book> getBooks() {
		return books;
	}

	public void setBooks(Collection<Book> books) {
		this.books = books;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;
		Author author = (Author) o;
		return Objects.equals(id, author.id) && Objects.equals(name, author.name);
	}

	@Override public int hashCode() {

		return Objects.hash(id, name);
	}
}
