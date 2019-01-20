package ru.otus.Domain;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
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
	private UUID id;
	private String name;
	@OneToMany
	private Collection<Genre> genres;

	public Author() {
		this.id = UUID.randomUUID();
	}

	public Author(UUID id) {
		this.id = id;
	}

	public Author(String name) {
		this();
		this.name = name;
	}

	public Author(UUID id ,String name) {
		this(id);
		this.name = name;
	}

	public Author(UUID id ,String name, List<Genre> genres) {
		this(id);
		this.name = name;
		this.genres = genres;
	}

	public UUID getId() {
		return id;
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
}
