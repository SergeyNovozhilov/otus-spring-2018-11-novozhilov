package ru.otus.Dao;

import ru.otus.Domain.Author;

import java.util.Collection;
import java.util.UUID;

public interface AuthorDao extends BaseDao{
	Collection<Author> getAll();
	Author getByName(String name);
	Author getById(UUID id);
	Collection<Author> getByBook(String book);
	Collection<Author> getByGenre(String genre);
	void save(Author author);
	int delete(Author author);
	int update(Author author);
	int deleteAll();
}
