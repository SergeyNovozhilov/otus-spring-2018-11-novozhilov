package ru.otus.Domain;

import lombok.Data;

import javax.persistence.*;
import java.util.Collection;
import java.util.HashSet;
import java.util.UUID;

@Data
@Table(name = "AUTHORS")
@Entity
public class Author extends Base{
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private UUID id;
	private String name;
	@Transient
	private Collection<Genre> genres = new HashSet<>();
	@ManyToMany(fetch=FetchType.EAGER, cascade = CascadeType.ALL, mappedBy = "authors")
	private Collection<Book> books;

	public Author() {
	}

	public Author(String name) {
		this.name = name;
	}

	public void addGenre(Genre genre) {
		if (this.genres == null) {
			this.genres = new HashSet<>();
		}
		this.genres.add(genre);
	}

	public void addGenres(Collection<Genre> genres) {
		if (this.genres == null) {
			this.genres = new HashSet<>();
		}
		this.genres.addAll(genres);
	}

	@Override
	public void print() {
		System.out.println(" Name: " + this.name);
		System.out.println("Genres:");
		if (this.genres != null && !this.genres.isEmpty()) {
			for (Genre genre : this.genres) {
				System.out.println("   " + genre.getName());
			}
		}
		System.out.println("Books:");
		if (this.books != null && !this.books.isEmpty()) {
			for (Book book : this.books) {
				System.out.println("   " + book.getTitle());
			}
		}
	}

	public Collection<Genre> getGenres() {
		if (this.genres == null) {
			this.genres = new HashSet<>();
		}
		return genres;
	}

	public UUID getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setGenres(Collection<Genre> genres) {
		this.genres = genres;
	}

	public Collection<Book> getBooks() {
		return books;
	}

	public void setBooks(Collection<Book> books) {
		this.books = books;
	}
}
