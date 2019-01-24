package ru.otus.DaoImpl;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.stereotype.Repository;
import ru.otus.Dao.AuthorDao;
import ru.otus.Domain.Author;
import ru.otus.Mapper.AuthorMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

@Repository
public class AuthorDaoJdbc implements AuthorDao {

	private NamedParameterJdbcOperations jdbc;

	public AuthorDaoJdbc(NamedParameterJdbcOperations jdbc) {
		this.jdbc = jdbc;
	}

	public static final String QUERY = "select a.id, a.name, g.id as genre_id, g.name as genre_name from AUTHORS a LEFT JOIN BOOKS_AUTHORS ba on a.id=ba.author LEFT JOIN BOOKS b on ba.book=b.id LEFT JOIN GENRES g on g.id=b.genre ";

	@Override
	public Collection<Author> getAll() {
		try {
			return jdbc.query(QUERY, new Extractor());
		} catch (DataAccessException e) {
			return new ArrayList<>();
		}
	}

	@Override
	public Author getByName(String name) {
		Map<String, String> params = Collections.singletonMap("name", name);
		try {
			Collection<Author> authors = jdbc.query(QUERY + "where a.name=:name", params, new Extractor());
			return authors.stream().findAny().orElse(null);
		} catch (DataAccessException e) {
			return null;
		}
	}

	@Override
	public Author getById(UUID id) {
		Map<String, UUID> params = Collections.singletonMap("id", id);
		try {
			Collection<Author> authors = jdbc.query(QUERY + "where a.id=:id",
					params, new Extractor());
			return authors.stream().findAny().orElse(null);
		} catch (DataAccessException e) {
			return null;
		}
	}

	@Override
	public Collection<Author> getByBook(String book) {
		Map<String, String> params = Collections.singletonMap("book", book);
		try {
			return jdbc.query(QUERY + "where b.title=:book",
					params, new Extractor());
		} catch (DataAccessException e) {
			return new ArrayList<>();
		}
	}

	@Override
	public Collection<Author> getByGenre(String genre) {
		Map<String, String> params = Collections.singletonMap("genre", genre);
		try {
			return jdbc.query("select a.id, a.name, g.id as genre_id, g.name as genre_name from AUTHORS a LEFT JOIN BOOKS_AUTHORS ba on a.id=ba.author LEFT JOIN BOOKS b on ba.book=b.id LEFT JOIN GENRES g on g.id=b.genre where a.id in (select a.id from AUTHORS a, BOOKS_AUTHORS ba, BOOKS b, GENRES g where g.name=:genre and b.genre = g.id and ba.book = b.id and a.id = ba.author)",
					params, new Extractor());
		} catch (DataAccessException e) {
			return new ArrayList<>();
		}
	}

	@Override
	public void save(Author author) {
		Map<String, String> params = new HashMap<>();
		params.put("id", author.getId().toString());
		params.put("name", author.getName());

		jdbc.update("insert into AUTHORS (id, name) " +
				"values (:id, :name)", params);
	}

	@Override
	public int delete(Author author) {
		Map<String, UUID> params = Collections.singletonMap("id", author.getId());
		int res = jdbc.queryForObject("select count(*) from BOOKS_AUTHORS where id=:id", params, Integer.class);
		if (res > 0) {
			return -1;
		}

		return jdbc.update("delete from AUTHORS " +
				"where id=:id", params);
	}

	@Override
	public int update(Author author) {
		Map<String, String> params = new HashMap<>();
		params.put("id", author.getId().toString());
		params.put("name", author.getName());

		return jdbc.update("update AUTHORS set name=:name where id=:id", params);
	}

	@Override
	public int deleteAll() {
		return jdbc.update("delete from AUTHORS", new HashMap<>());
	}

	class Extractor implements ResultSetExtractor<Collection<Author>> {
		@Override
		public Collection<Author> extractData(ResultSet rs) throws SQLException, DataAccessException {
			AuthorMapper authorMapper = new AuthorMapper();
			Map<UUID, Author> idAuthorMap = new HashMap<>();
			while(rs.next()){
				Author author = authorMapper.mapRow(rs,rs.getRow());
				Author a = idAuthorMap.get(author.getId());
				if (a != null && a.getGenres() != null) {
					a.addGenres(author.getGenres());
				} else {
					idAuthorMap.put(author.getId(), author);
				}
			}
			return idAuthorMap.values();
		}
	}
}
