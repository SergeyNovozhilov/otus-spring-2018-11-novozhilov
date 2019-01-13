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

	@Override
	public Collection<Author> getAll() {
		try {
			Collection<Author> authors = jdbc.query("select a.id as id, a.name as name, g.id as genre_id, g.name as genre_name " +
							"from AUTHORS a, GENRES g, BOOKS_AUTHORS ba " +
					"where a.id=ba.author " +
					"and g.id=ba.genre",
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
			Collection<Author> authors = jdbc.query("select a.id, a.name, g.id as genre_id, g.name as genre_name " +
					"from AUTHORS a, GENRES g, BOOKS_AUTHORS ba " +
					"where a.name=:name " +
					"and a.id=ba.author " +
					"and g.id=ba.genre",
					params, new AuthorMapper());
			return correctGenres(authors).stream().findAny().orElse(null);
		} catch (DataAccessException e) {
			return null;
		}
	}

	@Override
	public Author getById(UUID id) {
		Map<String, UUID> params = Collections.singletonMap("id", id);
		try {
			Collection<Author> authors = jdbc.query("select a.id, a.name, g.id as genre_id, g.name as genre_name " +
							"from AUTHORS a, GENRES g, BOOKS_AUTHORS ba " +
							"where a.id=:id " +
							"and a.id=ba.author " +
							"and g.id=ba.genre",
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
			Collection<Author> authors = jdbc.query("select a.id, a.name, g.id as genre_id, g.name as genre_name " +
							"from AUTHORS a, BOOKS_AUTHORS ba, BOOKS b, GENRES g" +
					"where b.title=:book " +
					"and b.id=ba.book " +
					"and a.id=ba.author " +
					"and g.id=ba.genre",
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
			Collection<Author> authors = jdbc.query("select a.id, a.name, g.id as genre_id, g.name as genre_name " +
					"from AUTHORS a, BOOKS_AUTHORS ba, GENRES g " +
					"where g.name=:genre " +
					"and g.id=ba.genre " +
					"and a.id=ba.author", params, new AuthorMapper());
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
		List<UUID> ids = all.stream().map(Author::getId).collect(toList());
		MapSqlParameterSource params = new MapSqlParameterSource();
		params.addValue("ids", ids);
		jdbc.update("delete from GENRES_AUTHORS " +
					"where author in (:ids) ", params);

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
