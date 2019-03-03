package ru.otus.Dtos;

import lombok.Data;

import java.util.*;

@Data
public class AuthorDto extends Base {
	private UUID id;
	private String name;
	private Collection<BookDto> books;

	public AuthorDto(String name) {
		this.name = name;
	}

	@Override
	public void print() {
		Set<String> genres = new HashSet<>();
		System.out.println(" Name: " + this.name);
		System.out.println("Books:");
		if (this.books != null && !this.books.isEmpty()) {
			for (BookDto book : this.books) {
				genres.add(book.getGenre().getName());
				System.out.println(" Title: " + book.getTitle());
				System.out.println("  GenreDto: " + Optional.ofNullable(book.getGenre()).orElse(new GenreDto()).getName());
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

	public Collection<BookDto> getBooks() {
		return books;
	}

	public void setBooks(Collection<BookDto> bookDtos) {
		this.books = bookDtos;
	}

	@Override public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;
		AuthorDto authorDto = (AuthorDto) o;
		return Objects.equals(id, authorDto.id) && Objects.equals(name, authorDto.name);
	}

	@Override public int hashCode() {

		return Objects.hash(id, name);
	}
}
