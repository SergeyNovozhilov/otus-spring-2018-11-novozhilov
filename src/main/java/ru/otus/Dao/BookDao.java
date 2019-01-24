package ru.otus.Dao;

import ru.otus.Domain.Book;

import java.util.Collection;
import java.util.UUID;

public interface BookDao extends BaseDao{
	Collection<Book> getAll();
	Collection<Book> getByTitle(String title);
	Book getById(UUID id);
	Collection<Book> getByAuthor(String author);
	Collection<Book> getByGenre(String genre);
	void save(Book book);
	void delete(Book book);
	Book update(Book book);
	int deleteAll();
}
