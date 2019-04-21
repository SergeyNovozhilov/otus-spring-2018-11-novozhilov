package ru.otus.document;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import ru.otus.entities.Author;
import ru.otus.entities.Comment;
import ru.otus.entities.Genre;

import java.util.Collection;
import java.util.HashSet;
import java.util.Objects;

@Data
@Document(collection = "Books")
public class Book {
	@Id
	private String id;
	private String title;
	private Collection<Author> authors;
	private Genre genre;
	private Collection<Comment> comments;

	public Book() {
	}

	public Book(String title) {
		this.title = title;
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

	public void addComment(Comment comment) {
		if (this.comments == null) {
			this.comments = new HashSet<>();
		}
		this.comments.add(comment);
	}

	public void removeComment(Comment comment) {
		if (this.comments == null) {
			return;
		}
		this.comments.remove(comment);
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
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

	public Collection<Comment> getComments() {
		return comments;
	}

	public void setComments(Collection<Comment> comments) {
		this.comments = comments;
	}

	@Override public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;
		Book book = (Book) o;
		if (!Objects.equals(id, book.id)) {
			return false;
		}
		if (!Objects.equals(title, book.title)) {
			return false;
		}
		if (!Objects.equals(genre, book.genre)) {
			return false;
		}
		return true;
	}

	@Override public int hashCode() {
		return Objects.hash(id, title, genre);
	}
}
