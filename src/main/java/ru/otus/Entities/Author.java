package ru.otus.Entities;

import lombok.Data;

import javax.persistence.*;
import java.util.*;

@Data
@Table(name = "AUTHORS")
@Entity
public class Author implements IPrint{
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private UUID id;
	private String name;
	@ManyToMany(fetch=FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.REFRESH, CascadeType.DETACH}, mappedBy = "authors")
	private Collection<Book> books;

	public Author() {
	}

	public Author(String name) {
		this.name = name;
	}

	public UUID getId() {
		return id;
	}

	public void setId(UUID id) {
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

	@Override
	public void print() {
		System.out.println(name);
	}
}
