package ru.otus.DaoImpl;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.stereotype.Repository;
import ru.otus.Dao.AuthorDao;
import ru.otus.Domain.Author;
import ru.otus.Domain.Genre;
import ru.otus.Mapper.AuthorMapper;

import java.util.*;
import java.util.function.Function;

import static java.util.stream.Collectors.*;

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
			Collection<Author> authors = jdbc.query(QUERY,
					new AuthorMapper());
			return correctGenres(authors);
		} catch (DataAccessException e) {
			return new ArrayList<>();
		}
	}

	@Override
	public Author getByName(String name) {
		Map<String, String> params = Collections.singletonMap("name", name);
		try {
			Collection<Author> authors = jdbc.query(QUERY + "where a.name=:name", params, new AuthorMapper());
			return correctGenres(authors).stream().findAny().orElse(null);
		} catch (DataAccessException e) {
			return null;
		}
	}

	@Override
	public Author getById(UUID id) {
		Map<String, UUID> params = Collections.singletonMap("id", id);
		try {
			Collection<Author> authors = jdbc.query(QUERY + "where a.id=:id",
					params, new AuthorMapper());
			return correctGenres(authors).stream().findAny().orElse(null);
		} catch (DataAccessException e) {
			return null;
		}
	}

	@Override
	public Collection<Author> getByBook(String book) {
		Map<String, String> params = Collections.singletonMap("book", book);
		try {
			Collection<Author> authors = jdbc.query(QUERY + "where b.title=:book",
					params, new AuthorMapper());
			return correctGenres(authors);
		} catch (DataAccessException e) {
			return new ArrayList<>();
		}
	}

	@Override
	public Collection<Author> getByGenre(String genre) {
		Map<String, String> params = Collections.singletonMap("genre", genre);
		try {
			Collection<Author> authors = jdbc.query(QUERY + "where g.name=:genre",
					params, new AuthorMapper());
			correctGenres(authors);
			return authors;
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
		Collection<Author> all =  this.getAll();
		if (all.isEmpty()) {
			return 0;
		}

		return jdbc.update("delete from AUTHORS", new HashMap<>());
	}

	private Collection<Author> correctGenres(Collection<Author> authorsList) {
		Collection<Author> authors = new HashSet<>();
		authorsList.stream().collect(groupingBy(Function.identity(), HashMap::new,
				mapping(Author::getGenres, toSet()))).forEach((k, v) -> {
			Collection<Genre> genres = new HashSet<>();
			v.forEach(x -> genres.addAll(x));
			k.addGenres(genres);
			authors.add(k);
		});
		return authors;
	}
}
