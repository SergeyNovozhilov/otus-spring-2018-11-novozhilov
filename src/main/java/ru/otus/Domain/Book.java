package ru.otus.Domain;

import lombok.Data;

import java.util.Collection;

@Data
public class Book extends Base{
	private String title;
	private Collection<Author> authors;
	private Genre genre;

	public Book(String title, Genre genre, Collection<Author> authors) {
		super();
		this.title = title;
		this.genre = genre;
		this.authors = authors;
	}
}
