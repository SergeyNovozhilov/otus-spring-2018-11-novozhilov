package ru.otus.Domain;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.UUID;

@Table(name = "AUTHORS")
@Entity
@Getter
@EqualsAndHashCode
public class Author extends Base{
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private UUID id;
	private String name;
	@OneToMany(cascade = CascadeType.ALL)
	private Collection<Genre> genres;
	@ManyToMany(cascade = CascadeType.ALL)
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
		return genres;
	}

	public void setGenres(Collection<Genre> genres) {
		this.genres = genres;
	}

	public Collection<Book> getBooks() {
		return books;
	}
}
