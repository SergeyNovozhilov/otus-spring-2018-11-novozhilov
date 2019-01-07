package ru.otus.DaoImpl;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.stereotype.Repository;
import ru.otus.Dao.GenreDao;
import ru.otus.Domain.Author;
import ru.otus.Domain.Genre;
import ru.otus.Mapper.GenreMapper;

import java.util.*;

import static java.util.stream.Collectors.toList;

@Repository
public class GenreDaoJdbc implements GenreDao {

	private NamedParameterJdbcOperations jdbc;

	public GenreDaoJdbc(NamedParameterJdbcOperations jdbc) {
		this.jdbc = jdbc;
	}

	@Override
	public Collection<Genre> getAll() {
		try {
			return jdbc.query("select id, name from GENRES", new GenreMapper());
		} catch (DataAccessException e) {
			return new ArrayList<>();
		}
	}

	@Override
	public Genre getByName(String name) {
		Map<String, String> params = Collections.singletonMap("name", name);
		try {
			return jdbc.queryForObject("select id, name from GENRES where name=:name", params, new GenreMapper());
		} catch (DataAccessException e) {
			return null;
		}
	}

	@Override
	public Genre getById(UUID id) {
		Map<String, UUID> params = Collections.singletonMap("id", id);
		try {
			return jdbc.queryForObject("select id, name from GENRES where id=:id", params, new GenreMapper());
		} catch (DataAccessException e) {
			return null;
		}
	}

	@Override
	public Collection<Genre> getByAuthor(String author) {
		Map<String, String> params = Collections.singletonMap("name", author);
		try {
			return jdbc.query("select g.id, g.name " +
							"from AUTHORS a, GENRES g, GENRES_AUTHORS ga " +
							"where a.name=:name " +
							"and a.id=ga.author " +
							"and g.id=ga.genre",
					params, new GenreMapper());
		} catch (DataAccessException e) {
			return new ArrayList<>();
		}
	}

	@Override
	public Genre getByBook(String book) {
		Map<String, String> params = Collections.singletonMap("book", book);
		try {
			return jdbc.queryForObject("select g.id, g.name " +
							"from GENRES g, BOOKS b " +
							"where b.title=:book " +
							"and g.id = b.genre",
					params, new GenreMapper());
		} catch (DataAccessException e) {
			return null;
		}
	}

	@Override
	public void save(Genre genre) {
		Map<String, String> params = new HashMap<>();
		params.put("id", genre.getId().toString());
		params.put("name", genre.getName());

		jdbc.update("insert into GENRES (id, name) " +
				"values (:id, :name)", params);
	}

	@Override
	public int delete(Genre genre) {
		Map<String, UUID> params = Collections.singletonMap("id", genre.getId());
		int res = jdbc.queryForObject("select count(*) from GENRES_AUTHORS where id=:id", params, Integer.class);
		res += jdbc.queryForObject("select count(*) from BOOKS where genre=:id", params, Integer.class);

		if (res > 0) {
			return -1;
		}

		return jdbc.update("delete from GENRES " +
				"where id=:id", params);
	}

	@Override
	public int update(Genre genre) {
		Map<String, String> params = new HashMap<>();
		params.put("id", genre.getId().toString());
		params.put("name", genre.getName());
		return jdbc.update("update GENRES set name=:name where id=:id", params);
	}

	@Override
	public int deleteAll() {
		Collection<Genre> all =  this.getAll();
		if (all.isEmpty()) {
			return 0;
		}
		List<UUID> ids = all.stream().map(Genre::getId).collect(toList());
		MapSqlParameterSource params = new MapSqlParameterSource();
		params.addValue("ids", ids);
		jdbc.update("delete from GENRES_AUTHORS " +
				"where genre in (:ids) ", params);

		return jdbc.update("delete from GENRES", new HashMap<>());
	}
}
