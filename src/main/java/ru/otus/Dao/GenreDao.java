package ru.otus.Dao;

import ru.otus.Domain.Genre;

import java.util.Collection;
import java.util.UUID;

public interface GenreDao extends BaseDao{
	Collection<Genre> getAll();
	Genre getByName(String name);
	Genre getById(UUID id);
	Collection<Genre> getByAuthor(String author);
	Genre getByBook(String book);
	void save(Genre genre);
	void delete(Genre genre);
	Genre update(Genre genre);
	int deleteAll();
}
