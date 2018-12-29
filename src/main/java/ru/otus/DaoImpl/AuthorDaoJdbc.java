package ru.otus.DaoImpl;

import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.stereotype.Repository;
import ru.otus.Dao.AuthorDao;
import ru.otus.Domain.Author;
import ru.otus.Domain.Book;
import ru.otus.Domain.Genre;

import java.util.Collection;

@Repository
public class AuthorDaoJdbc implements AuthorDao {

	private NamedParameterJdbcOperations jdbc;

	public AuthorDaoJdbc(NamedParameterJdbcOperations jdbc) {
		this.jdbc = jdbc;
	}

	@Override
	public Author getByName(String name) {
		return null;
	}

	@Override
	public Author getById(int id) {
		return null;
	}

	@Override
	public Collection<Author> getByBook(Book book) {
		return null;
	}

	@Override
	public Collection<Author> getByGenre(Genre genre) {
		return null;
	}

	@Override
	public void save(Author author) {

	}

	@Override
	public void delete(Author author) {

	}

	@Override
	public void update(Author author) {

	}
}
