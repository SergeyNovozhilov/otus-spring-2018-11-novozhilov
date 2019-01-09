package ru.otus.Domain;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.UUID;

@Getter
@EqualsAndHashCode(exclude = "genres")
public class Author extends Base{
	private String name;
	private Collection<Genre> genres;

	public Author(String name) {
		super();
		this.name = name;
	}

	public Author(String name, List<Genre> genres) {
		super();
		this.name = name;
		this.genres = genres;
	}

	public Author(UUID id ,String name) {
		super(id);
		this.name = name;
	}

	public Author(UUID id ,String name, List<Genre> genres) {
		super(id);
		this.name = name;
		this.genres = genres;
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
