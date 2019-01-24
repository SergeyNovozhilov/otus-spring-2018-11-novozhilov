package ru.otus.DaoImpl;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.stereotype.Repository;
import ru.otus.Dao.BookDao;
import ru.otus.Domain.Author;
import ru.otus.Domain.Book;
import ru.otus.Mapper.BookMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

@Repository
public class BookDaoJdbc implements BookDao {

	private NamedParameterJdbcOperations jdbc;

	public BookDaoJdbc(NamedParameterJdbcOperations jdbc) {
		this.jdbc = jdbc;
	}

	public static final String QUERY = "select b.id as id, b.title as title, g.id as genre_id, g.name as genre_name, a.id as author_id, a.name as author_name from BOOKS b left join GENRES g on g.id = b.genre left join BOOKS_AUTHORS ba on ba.book=b.id left join AUTHORS a on a.id=ba.author ";

	@Override
	public Collection<Book> getAll() {
		try {
			return jdbc.query(QUERY, new Extractor());
		} catch (DataAccessException e) {
			return new ArrayList<>();
		}
	}

	@Override
	public Collection<Book> getByTitle(String title) {
		Map<String, String> params = Collections.singletonMap("title", title);
		try {
            return jdbc.query(QUERY + "where b.title=:title",
					params, new Extractor());
        } catch (DataAccessException e) {
		    return new ArrayList<>();
        }
	}

	@Override
	public Book getById(UUID id) {
		Map<String, UUID> params = Collections.singletonMap("id", id);
		try {
			Collection<Book> books = jdbc.query(QUERY + "where b.id=:id",
					params, new Extractor());
			return books.stream().findFirst().orElse(null);
		} catch (DataAccessException e) {
			return null;
		}
	}

	@Override
	public Collection<Book> getByAuthor(String author) {
		Map<String, String> params = Collections.singletonMap("author", author);
		try {
			return jdbc.query("select b.id as id, b.title as title, g.id as genre_id, g.name as genre_name, a.id as author_id, a.name as author_name from BOOKS b left join GENRES g on g.id = b.genre left join BOOKS_AUTHORS ba on ba.book=b.id left join AUTHORS a on a.id=ba.author where b.id in (select b.id from BOOKS b, BOOKS_AUTHORS ba, AUTHORS a where a.name=:author and a.id=ba.author and ba.book=b.id) ", params, new Extractor());
		} catch (DataAccessException e) {
			return new ArrayList<>();
		}
	}

	@Override
	public Collection<Book> getByGenre(String genre) {
		Map<String, String> params = Collections.singletonMap("genre", genre);
		try {
			return jdbc.query(QUERY + "where g.name=:genre",
					params, new Extractor());
		} catch (DataAccessException e) {
			return new ArrayList<>();
		}
	}

	@Override
	public void save(Book book) {
		Map<String, String> params = new HashMap<>();
		params.put("id", book.getId().toString());
		params.put("title", book.getTitle());
		if (book.getGenre() != null) {
			String genreId = book.getGenre().getId().toString();
			params.put("genre", genreId);
			jdbc.update("insert into BOOKS (id, title, genre)" + "values (:id, :title, :genre)", params);
		} else {
			jdbc.update("insert into BOOKS (id, title)" + "values (:id, :title)", params);
		}

		if (book.getAuthors() != null && !book.getAuthors().isEmpty()) {
			List<Map<String, Object>> batchValues = new ArrayList<>(book.getAuthors().size());
			for (Author author : book.getAuthors()) {
				batchValues.add(
						new MapSqlParameterSource("id", UUID.randomUUID()).addValue("book", book.getId()).addValue("author", author.getId()).getValues());
			}

			jdbc.batchUpdate("insert into BOOKS_AUTHORS (id, book, author) " +
					"values (:id, :book, :author)", batchValues.toArray(new Map[book.getAuthors().size()]));
		}

	}

	@Override
	public int delete(Book book) {
		List<Map<String, Object>> batchValues = new ArrayList<>(book.getAuthors().size());
		for (Author author : book.getAuthors()) {
			batchValues.add(
					new MapSqlParameterSource("book", book.getId()).addValue("author", author.getId()).getValues());
		}

		jdbc.batchUpdate("delete from BOOKS_AUTHORS " +
				"where book=:book " +
				"and author=:author", batchValues.toArray(new Map[book.getAuthors().size()]));

		Map<String, UUID> params = Collections.singletonMap("id", book.getId());
		return jdbc.update("delete from BOOKS " +
				"where id=:id", params);
	}

	@Override
	public int update(Book book) {
		Map<String, String> params = new HashMap<>();
		params.put("id", book.getId().toString());
		params.put("title", book.getTitle());
		String genreId = book.getGenre() != null ? book.getGenre().getId().toString() : "NULL";
		params.put("genre", genreId);

		Book old = getById(book.getId());

		if (old != null && old.getAuthors() != null && !old.getAuthors().isEmpty()) {
			List<Map<String, Object>> batchValues = new ArrayList<>(old.getAuthors().size());
			for (Author author : old.getAuthors()) {
				batchValues.add(
						new MapSqlParameterSource("book", old.getId()).addValue("author", author.getId()).getValues());
			}

			jdbc.batchUpdate("delete from BOOKS_AUTHORS " +
					"where book=:book " +
					"and author=:author", batchValues.toArray(new Map[old.getAuthors().size()]));
		}

		if (book.getAuthors() != null && !book.getAuthors().isEmpty()) {
			List<Map<String, Object>> batch = new ArrayList<>(book.getAuthors().size());
			for (Author author : book.getAuthors()) {
				batch.add(
						new MapSqlParameterSource("id", UUID.randomUUID()).addValue("book", book.getId()).addValue("author", author.getId()).getValues());
			}

			jdbc.batchUpdate("insert into BOOKS_AUTHORS (id, book, author) " +
					"values (:id, :book, :author)", batch.toArray(new Map[book.getAuthors().size()]));
		}
		if (book.getGenre() != null) {
			String id = book.getGenre().getId().toString();
			params.put("genre", id);
			return jdbc.update("update BOOKS set title=:title, genre=:genre where id=:id", params);
		} else {
			return jdbc.update("update BOOKS set title=:title where id=:id", params);
		}
	}

	@Override
	public int deleteAll() {
		return jdbc.update("delete from BOOKS", new HashMap<>());
	}

	class Extractor implements ResultSetExtractor<Collection<Book>> {
		@Override
		public Collection<Book> extractData(ResultSet rs) throws SQLException, DataAccessException {
			BookMapper bookMapper = new BookMapper();
			Map<UUID, Book> idBookMap = new HashMap<>();
			while(rs.next()){
				Book book = bookMapper.mapRow(rs,rs.getRow());
				Book b = idBookMap.get(book.getId());
				if (b != null) {
					b.addAuthors(book.getAuthors());
				} else {
					idBookMap.put(book.getId(), book);
				}
			}
			return idBookMap.values();
		}
	}
}
