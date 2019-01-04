package ru.otus.DaoImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSourceUtils;
import org.springframework.stereotype.Repository;
import ru.otus.Dao.BookDao;
import ru.otus.Domain.Author;
import ru.otus.Domain.Book;
import ru.otus.Domain.Genre;
import ru.otus.Mapper.BookMapper;

import java.util.*;
import java.util.stream.Collectors;

@Repository
public class BookDaoJdbc implements BookDao {

	private NamedParameterJdbcOperations jdbc;

	public BookDaoJdbc(NamedParameterJdbcOperations jdbc) {
		this.jdbc = jdbc;
	}

	@Override
	public Collection<Book> getAll() {
		Collection<Book> books = null;
		try {
			books = jdbc.query("select b.id as id, b.title as title, g.id as genre_id, g.name as genre_name " +
							"from BOOKS b, GENRES g " +
							"where g.id = b.genre",
							new BookMapper());
		} catch (DataAccessException e) {
			return new ArrayList<>();
		}
		setAuthors(books);
		return books;
	}

	@Override
	public Collection<Book> getByTitle(String title) {
		Map<String, String> params = Collections.singletonMap("title", title);
        Collection<Book> books = null;
		try {
            books = jdbc.query("select b.id as id, b.title as title, g.id as genre_id, g.name as genre_name " +
					"from GENRES g, BOOKS b " +
					"where b.title=:title " +
					"and g.id = b.genre",
					params, new BookMapper());
        } catch (DataAccessException e) {
		    return new ArrayList<>();
        }
        setAuthors(books);
		return books;
	}

	@Override
	public Book getByAuthorAndTitle(String author, String title) {
        Map<String, String> params = new HashMap<>();
        params.put("author_name", author);
        params.put("title", title);
		try {
			Book book = jdbc.queryForObject("select b.id as id, b.title as title, g.id as genre_id, g.name as genre_name " +
					"from GENRES g, BOOKS b, AUTHORS a, BOOKS_AUTHORS ba " +
					"where b.title=:title " +
					"and a.name=:author_name " +
					"and ba.book = b.id " +
					"and a.id=ba.author " +
					"and g.id = b.genre",
					params, new BookMapper());
			setAuthors(book);
			return book;
		} catch (DataAccessException e) {
			return null;
		}
	}

	@Override
	public Book getById(UUID id) {
		Map<String, UUID> params = Collections.singletonMap("id", id);
		try {
			Book book = jdbc.queryForObject("select b.id as id, b.title as title, g.id as genre_id, g.name as genre_name " +
					"from GENRES g, BOOKS b " +
					"where b.id=:id " +
					"and g.id = b.genre",
					params, new BookMapper());
			setAuthors(book);
			return book;
		} catch (DataAccessException e) {
			return null;
		}
	}

	@Override
	public Collection<Book> getByAuthor(String author) {
		Map<String, String> params = Collections.singletonMap("author", author);
		Collection<Book> books = null;
		try {
			books = jdbc.query("select b.id as id, b.title as title, g.id as genre_id, g.name as genre_name " +
							"from GENRES g, BOOKS b, AUTHORS a, BOOKS_AUTHORS ba " +
							"where a.name=:author " +
							"and a.id=ba.author " +
							"and b.id=ba.book " +
							"and g.id = b.genre",
					params, new BookMapper());
		} catch (DataAccessException e) {
			return new ArrayList<>();
		}
		setAuthors(books);
		return books;
	}

	@Override
	public Collection<Book> getByGenre(String genre) {
		Map<String, String> params = Collections.singletonMap("genre", genre);
		Collection<Book> books = null;
		try {
			books = jdbc.query("select b.id as id, b.title as title, g.id as genre_id, g.name as genre_name " +
							"from GENRES g, BOOKS b " +
							"where g.name=:genre " +
							"and g.id = b.genre",
					params, new BookMapper());
		} catch (DataAccessException e) {
			return new ArrayList<>();
		}
		setAuthors(books);
		return books;
	}

	@Override
	public void save(Book book) {
		Map<String, String> params = new HashMap<>();
		params.put("id", book.getId().toString());
		params.put("title", book.getTitle());
		params.put("genre", book.getGenre().getId().toString());
		jdbc.update("insert into BOOKS (id, title, genre)" +
				"values (:id, :title, :genre)", params);

		List<MapSqlParameterSource> batchArgs = new ArrayList<>();

		for (Author author : book.getAuthors()) {
			MapSqlParameterSource parameters = new MapSqlParameterSource();
			parameters.addValue("id", UUID.randomUUID());
			parameters.addValue("book", book.getId());
			parameters.addValue("author", author.getId());
			batchArgs.add(parameters);
		}
		SqlParameterSource[] batch = SqlParameterSourceUtils.createBatch(batchArgs.toArray());

		jdbc.batchUpdate("insert into BOOKS_AUTHORS (id, book, author) " +
				"values (:id, :book, :author)", batch);
	}

	@Override
	public int delete(Book book) {
		List<MapSqlParameterSource> batchArgs = new ArrayList<>();
		for (Author author : book.getAuthors()) {
			MapSqlParameterSource parameters = new MapSqlParameterSource();
			parameters.addValue("book", book.getId());
			parameters.addValue("author", author.getId());
			batchArgs.add(parameters);
		}
		SqlParameterSource[] batch = SqlParameterSourceUtils.createBatch(batchArgs.toArray());
		jdbc.batchUpdate("delete from BOOKS_AUTHORS " +
				"where book=:book " +
				"and author=:author", batch);

		Map<String, UUID> params = Collections.singletonMap("id", book.getId());
		return jdbc.update("delete from BOOKS " +
				"where id=:id", params);
	}

	@Override
	public int update(Book book) {
		Map<String, String> params = new HashMap<>();
		params.put("id", book.getId().toString());
		params.put("title", book.getTitle());
		params.put("genre", book.getGenre().getId().toString());

		Book old = getById(book.getId());

		List<MapSqlParameterSource> batchArgs = new ArrayList<>();
		for (Author author : old.getAuthors()) {
			MapSqlParameterSource parameters = new MapSqlParameterSource();
			parameters.addValue("book", book.getId());
			parameters.addValue("author", author.getId());
			batchArgs.add(parameters);
		}
		SqlParameterSource[] batch = SqlParameterSourceUtils.createBatch(batchArgs.toArray());
		jdbc.batchUpdate("delete from BOOKS_AUTHORS " +
				"where book=:book " +
				"and author=:author", batch);

		List<MapSqlParameterSource> authorsBatchArgs = new ArrayList<>();

		for (Author author : book.getAuthors()) {
			MapSqlParameterSource parameters = new MapSqlParameterSource();
			parameters.addValue("id", UUID.randomUUID());
			parameters.addValue("book", book.getId());
			parameters.addValue("author", author.getId());
			authorsBatchArgs.add(parameters);
		}
		SqlParameterSource[] authorsBatch = SqlParameterSourceUtils.createBatch(authorsBatchArgs.toArray());

		jdbc.batchUpdate("insert into BOOKS_AUTHORS (id, book, author) " +
				"values (:id, :book, :author)", authorsBatch);

		return jdbc.update("update BOOKS set title=:title genre:=genre where id=:id", params);
	}

	private void setAuthors(Book book) {
		setAuthors(Collections.singletonList(book));
	}

	private void setAuthors(Collection<Book> books) {
        List<UUID> ids = books.stream().map(Book::getId).collect(Collectors.toList());
        List<String> stringIds = ids.stream().map(String::valueOf).collect(Collectors.toList());
        Map<String, String> params = Collections.singletonMap("ids", String.join(",", stringIds));
        List<Map<String, Object>> rows = jdbc.queryForList("select ba.book as book_id, ba.author as author_id, a.name as author_name " +
				"from BOOKS_AUTHORS ba, AUTHORS a " +
				"where ba.book in (:ids) " +
				"and ba.author = a.id", params);
        for (Map<String, Object> row : rows) {
            UUID bookId = (UUID)row.get("book_id");
            UUID authorId = (UUID)row.get("author_id");
            String authorName = (String)row.get("author_name");

            Author author = new Author(authorId, authorName);
            Book book = books.stream().filter(x -> x.getId().equals(bookId)).findAny().orElse(null);
            if (book != null) {
                book.addAuthor(author);
            }
        }
    }
}
