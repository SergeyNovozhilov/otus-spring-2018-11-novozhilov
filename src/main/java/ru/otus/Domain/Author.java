package ru.otus.Domain;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import java.util.*;

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
		System.out.println("Books:");
		if (this.books != null) {
			for (Book book : this.books) {
				System.out.println("   " + book.getTitle());
				System.out.println(" Genre:   " + book.getGenre().getName());
			}
		}
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

	public Collection<Genre> getGenres() {
		return genres;
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

	@Override public boolean equals(Object o) {
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
