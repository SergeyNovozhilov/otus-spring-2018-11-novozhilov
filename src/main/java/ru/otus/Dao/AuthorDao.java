package ru.otus.Dao;

import ru.otus.Domain.Author;
import ru.otus.Domain.Book;
import ru.otus.Domain.Genre;

import java.util.Collection;

public interface AuthorDao {
	Author getByName(String name);
	Author getById(int id);
	Collection<Author> getByBook(Book book);
	Collection<Author> getByGenre(Genre genre);
	void save(Author author);
	void delete(Author author);
	void update(Author author);
}
