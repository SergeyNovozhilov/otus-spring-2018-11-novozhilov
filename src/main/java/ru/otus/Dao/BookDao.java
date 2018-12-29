package ru.otus.Dao;

import ru.otus.Domain.Author;
import ru.otus.Domain.Book;
import ru.otus.Domain.Genre;

import java.util.Collection;

public interface BookDao {
	Collection<Book> getByTitle(String title);
	Book getByAuthorAndTitle(Author author, String title);
	Book getById(int id);
	Collection<Book> getByAuthor(Author author);
	Collection<Book> getByGenre(Genre genre);
	void save(Book book);
	void delete(Book author);
	void update(Book author);
}
