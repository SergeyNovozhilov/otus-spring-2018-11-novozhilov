package ru.otus.Domain;

import javax.persistence.*;
import java.util.Collection;
import java.util.HashSet;
import java.util.UUID;

@Table(name = "BOOKS")
@Entity
public class Book extends Base{
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private UUID id;
	private String title;
	@ManyToMany(fetch=FetchType.EAGER, cascade = CascadeType.ALL)
	private Collection<Author> authors;
	@OneToOne(cascade = CascadeType.ALL)
	private Genre genre;

	public Book() {
	}

	public Book(String title) {
		this.title = title;
		this.authors = new HashSet<>();
	}

	public UUID getId() {
		return id;
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
		if (this.authors != null) {
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
}
