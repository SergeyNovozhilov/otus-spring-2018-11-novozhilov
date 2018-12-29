package ru.otus.Dao;

import ru.otus.Domain.Author;
import ru.otus.Domain.Book;
import ru.otus.Domain.Genre;

import java.util.Collection;

public interface GenreDao {
	Genre getByName(String name);
	Genre getById(int id);
	Collection<Genre> getByAuthor(Author author);
	Collection<Genre> getByBook(Book book);
	void save(Genre genre);
	void delete(Genre genre);
	void update(Genre genre);
}
