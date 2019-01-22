package ru.otus.Domain;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.*;

@Data
@EqualsAndHashCode(exclude = {"authors", "genre"})
public class Book extends Base{
	private String title;
	private Collection<Author> authors;
	private Genre genre;

	public Book(String title) {
		super();
		this.title = title;
		this.authors = new HashSet<>();
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

	public void addAuthors(Collection<Author> authors) {
		if (this.authors == null) {
			this.authors = new HashSet<>();
		}
		this.authors.addAll(authors);
	}

	@Override
	public void print() {
		System.out.println(" Title: " + this.title);
		System.out.println(" Genre: " + this.genre.getName());
		System.out.println("Authors:");
		if (this.authors != null && !this.authors.isEmpty()) {
			for (Author author : this.authors) {
				System.out.println("   " + author.getName());
			}
		}
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

	@Override public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;
		Book book = (Book) o;
		return Objects.equals(title, book.title)/* && Objects.equals(genre, book.genre)*/;
	}

	@Override public int hashCode() {

		return Objects.hash(title/*, genre*/);
	}
}
