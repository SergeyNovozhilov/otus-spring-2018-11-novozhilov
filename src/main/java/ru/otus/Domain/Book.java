package ru.otus.Domain;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.UUID;

@Data
@EqualsAndHashCode
public class Book extends Base{
	private String title;
	private Collection<Author> authors;
	private Genre genre;

	public Book(String title, Genre genre) {
		super();
		this.title = title;
		this.genre = genre;
		this.authors = new HashSet<>();
	}

    public Book(String title, Genre genre, Collection<Author> authors) {
        super();
        this.title = title;
        this.genre = genre;
        this.authors = authors;
    }

	public Book(UUID id, String title, Genre genre) {
		super(id);
		this.title = title;
		this.genre = genre;
		this.authors = new HashSet<>();
	}

	public void addAuthor(Author author) {
	    if (this.authors == null) {
	        this.authors = new HashSet<>();
        }
	    this.authors.add(author);
    }

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public Collection<Author> getAuthors() {
		return authors;
	}

	public void setAuthors(Collection<Author> authors) {
		this.authors = authors;
	}

	public Genre getGenre() {
		return genre;
	}

	public void setGenre(Genre genre) {
		this.genre = genre;
	}
}
