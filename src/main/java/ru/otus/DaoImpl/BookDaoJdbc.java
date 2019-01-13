package ru.otus.DaoImpl;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.stereotype.Repository;
import ru.otus.Dao.BookDao;
import ru.otus.Domain.Author;
import ru.otus.Domain.Book;
import ru.otus.Domain.Genre;
import ru.otus.Mapper.BookMapper;

import java.util.*;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

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

		List<Map<String, Object>> batchValues = new ArrayList<>(book.getAuthors().size());
		for (Author author : book.getAuthors()) {
			batchValues.add(
					new MapSqlParameterSource("id", UUID.randomUUID()).addValue("book", book.getId()).addValue("author", author.getId()).getValues());
		}

		jdbc.batchUpdate("insert into BOOKS_AUTHORS (id, book, author) " +
				"values (:id, :book, :author)", batchValues.toArray(new Map[book.getAuthors().size()]));

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
		params.put("genre", book.getGenre().getId().toString());

		Book old = getById(book.getId());

		List<Map<String, Object>> batchValues = new ArrayList<>(old.getAuthors().size());
		for (Author author : old.getAuthors()) {
			batchValues.add(
					new MapSqlParameterSource("book", old.getId()).addValue("author", author.getId()).getValues());
		}

		jdbc.batchUpdate("delete from BOOKS_AUTHORS " +
				"where book=:book " +
				"and author=:author", batchValues.toArray(new Map[old.getAuthors().size()]));

		List<Map<String, Object>> batch = new ArrayList<>(book.getAuthors().size());
		for (Author author : book.getAuthors()) {
			batch.add(
					new MapSqlParameterSource("id", UUID.randomUUID()).addValue("book", book.getId()).addValue("author", author.getId()).getValues());
		}

		jdbc.batchUpdate("insert into BOOKS_AUTHORS (id, book, author) " +
				"values (:id, :book, :author)", batch.toArray(new Map[book.getAuthors().size()]));

		return jdbc.update("update BOOKS set title=:title, genre=:genre where id=:id", params);
	}

	@Override
	public int deleteAll() {
		Collection<Book> all =  this.getAll();
		if (all.isEmpty()) {
			return 0;
		}
		List<UUID> ids = all.stream().map(Book::getId).collect(toList());
		MapSqlParameterSource params = new MapSqlParameterSource();
		params.addValue("ids", ids);
		jdbc.update("delete from BOOKS_AUTHORS " +
				"where book in (:ids) ", params);
		return jdbc.update("delete from BOOKS", new HashMap<>());
	}

	private void setAuthors(Book book) {
		setAuthors(Collections.singletonList(book));
	}

	private void setAuthors(Collection<Book> books) {
        List<UUID> ids = books.stream().map(Book::getId).collect(Collectors.toList());
		MapSqlParameterSource params = new MapSqlParameterSource();
		params.addValue("ids", ids);
        List<Map<String, Object>> rows = jdbc.queryForList("select ba.book as book_id, ba.author as author_id, a.name as author_name, g.id as genre_id, g.name as genre_name " +
				"from BOOKS_AUTHORS ba, AUTHORS a, GENRES g, GENRES_AUTHORS ga " +
				"where ba.book in (:ids) " +
				"and ba.author = a.id " +
						"and ga.author=a.id " +
						"and g.id=ga.genre",
				params);
        for (Map<String, Object> row : rows) {
            UUID bookId = (UUID)row.get("book_id");
            UUID authorId = (UUID)row.get("author_id");
            String authorName = (String)row.get("author_name");
            UUID genreId = (UUID)row.get("genre_id");
			String genreName = (String)row.get("genre_name");

			Genre genre = new Genre(genreId, genreName);
            Book book = books.stream().filter(x -> x.getId().equals(bookId)).findAny().orElse(null);
            if (book != null) {
            	Author author = book.getAuthors().stream().filter(a -> a.getId().equals(authorId)).findAny().orElse(null);
            	if (author == null) {
					author = new Author(authorId, authorName);
					book.addAuthor(author);
				}
            	author.addGenre(genre);
            }
        }
    }
}
