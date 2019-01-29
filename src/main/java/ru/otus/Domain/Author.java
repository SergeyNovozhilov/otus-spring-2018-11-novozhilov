package ru.otus.Domain;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import java.util.Collection;
import java.util.HashSet;
import java.util.UUID;

@Table(name = "AUTHORS")
@Entity
public class Author extends Base{
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private UUID id;
	private String name;
	@Transient
	private Collection<Genre> genres;
	@ManyToMany(fetch=FetchType.EAGER, cascade = CascadeType.ALL, mappedBy = "authors")
	@OnDelete(action = OnDeleteAction.CASCADE)
	private Collection<Book> books;

	public Author() {
	}

	public Author(String name) {
		this.name = name;
	}

	public UUID getId() {
		return id;
	}


	@Override
	public void print() {
		System.out.println(" Name: " + this.name);
		System.out.println("Genres:");
		if (this.genres != null) {
			for (Genre genre : this.genres) {
				System.out.println("   " + genre.getName());
			}
		}
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Collection<Genre> getGenres() {
		if (this.genres == null) {
			this.genres = new HashSet<>();
		}
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
}
