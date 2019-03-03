package ru.otus.Dtos;

import lombok.Data;

import java.util.Collection;
import java.util.HashSet;
import java.util.Objects;
import java.util.UUID;

@Data
public class BookDto extends Base {
	private UUID id;
	private String title;
	private Collection<AuthorDto> authors;
	private GenreDto genre;
	private Collection<CommentDto> comments;

	public BookDto(String title) {
		this.title = title;
	}

	public void addAuthor(AuthorDto authorDto) {
	    if (this.authors == null) {
	        this.authors = new HashSet<>();
        }
	    this.authors.add(authorDto);
    }

	public void addAuthors(Collection<AuthorDto> authorDtos) {
		if (this.authors == null) {
			this.authors = new HashSet<>();
		}
		this.authors.addAll(authorDtos);
	}

	@Override
	public void print() {
		System.out.println(" Title: " + this.title);
		System.out.println(" GenreDto: " + this.genre.getName());
		System.out.println("Authors:");
		if (this.authors != null && !this.authors.isEmpty()) {
			for (AuthorDto authorDto : this.authors) {
				System.out.println("   " + authorDto.getName());
			}
		}
		System.out.println("Comments:");
		if (this.comments != null && !this.comments.isEmpty()) {
			for (CommentDto commentDto : this.comments) {
				System.out.println("   " + commentDto.getComment());
			}
		}
	}

	public void addComment(CommentDto commentDto) {
		if (this.comments == null) {
			this.comments = new HashSet<>();
		}
		this.comments.add(commentDto);
	}

	public void removeComment(CommentDto commentDto) {
		if (this.comments == null) {
			return;
		}
		this.comments.remove(commentDto);
	}

	public UUID getId() {
		return id;
	}

	public void setId(UUID id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public Collection<AuthorDto> getAuthors() {
		return authors;
	}

	public void setAuthors(Collection<AuthorDto> authorDtos) {
		this.authors = authorDtos;
	}

	public GenreDto getGenre() {
		return genre;
	}

	public void setGenre(GenreDto genreDto) {
		this.genre = genreDto;
	}

	public Collection<CommentDto> getComments() {
		return comments;
	}

	public void setComments(Collection<CommentDto> commentDtos) {
		this.comments = commentDtos;
	}

	@Override public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;
		BookDto bookDto = (BookDto) o;
		if (!Objects.equals(id, bookDto.id)) {
			return false;
		}
		if (!Objects.equals(title, bookDto.title)) {
			return false;
		}
		if (!Objects.equals(genre, bookDto.genre)) {
			return false;
		}
		return true;
	}

	@Override public int hashCode() {

		return Objects.hash(id, title, genre);
	}
}
