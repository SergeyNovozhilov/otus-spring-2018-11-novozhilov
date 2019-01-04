package ru.otus.Dao;

import ru.otus.Domain.Genre;

import java.util.Collection;
import java.util.UUID;

public interface GenreDao extends BaseDao{
	Collection<Genre> getAll();
	Genre getByName(String name);
	Genre getById(UUID id);
	Collection<Genre> getByAuthor(String author);
	Collection<Genre> getByBook(String book);
	void save(Genre genre);
	int delete(Genre genre);
	int update(Genre genre);
}
