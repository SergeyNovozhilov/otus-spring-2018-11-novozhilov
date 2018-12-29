package ru.otus.DaoImpl;

import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.stereotype.Repository;
import ru.otus.Dao.BookDao;
import ru.otus.Domain.Author;
import ru.otus.Domain.Book;
import ru.otus.Domain.Genre;
import ru.otus.Mapper.BookMapper;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;

@Repository
public class BookDaoJdbc implements BookDao {

	private NamedParameterJdbcOperations jdbc;

	public BookDaoJdbc(NamedParameterJdbcOperations jdbc) {
		this.jdbc = jdbc;
	}

	@Override
	public Collection<Book> getByTitle(String title) {
		Map<String, String> params = Collections.singletonMap("title", title);
		return jdbc.query("select b.id as id, b.title as title, a.id as author_id, a.name as author_name from books b, authors a where b.title=:title and a.id = b.author", params, new BookMapper());
	}

	@Override
	public Book getByAuthorAndTitle(Author author, String title) {
		return null;
	}

	@Override
	public Book getById(int id) {
		return null;
	}

	@Override
	public Collection<Book> getByAuthor(Author author) {
		return null;
	}

	@Override
	public Collection<Book> getByGenre(Genre genre) {
		return null;
	}

	@Override
	public void save(Book book) {

	}

	@Override
	public void delete(Book author) {

	}

	@Override
	public void update(Book author) {

	}
}
