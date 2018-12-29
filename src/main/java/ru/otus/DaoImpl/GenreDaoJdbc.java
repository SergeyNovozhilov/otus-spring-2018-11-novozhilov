package ru.otus.DaoImpl;

import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.stereotype.Repository;
import ru.otus.Dao.GenreDao;
import ru.otus.Domain.Author;
import ru.otus.Domain.Book;
import ru.otus.Domain.Genre;

import java.util.Collection;

@Repository
public class GenreDaoJdbc implements GenreDao {

	private NamedParameterJdbcOperations jdbc;

	public GenreDaoJdbc(NamedParameterJdbcOperations jdbc) {
		this.jdbc = jdbc;
	}

	@Override
	public Genre getByName(String name) {
		return null;
	}

	@Override
	public Genre getById(int id) {
		return null;
	}

	@Override
	public Collection<Genre> getByAuthor(Author author) {
		return null;
	}

	@Override
	public Collection<Genre> getByBook(Book book) {
		return null;
	}

	@Override
	public void save(Genre genre) {

	}

	@Override
	public void delete(Genre genre) {

	}

	@Override
	public void update(Genre genre) {

	}
}
