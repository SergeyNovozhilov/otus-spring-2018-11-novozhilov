package ru.otus.DaoImpl;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.Dao.BookDao;
import ru.otus.Domain.Author;
import ru.otus.Domain.Book;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.*;
import java.util.function.Function;

import static java.util.stream.Collectors.*;

@Repository
public class BookDaoJpa implements BookDao {

	@PersistenceContext
	private EntityManager em;

	public static final String QUERY = "select b.id as id, b.title as title, g.id as genre_id, g.name as genre_name, a.id as author_id, a.name as author_name from BOOKS b left join GENRES g on g.id = b.genre left join BOOKS_AUTHORS ba on ba.book=b.id left join AUTHORS a on a.id=ba.author ";

	@Override
	public Collection<Book> getAll() {
		Collection<Book> books = null;
//		try {
//			books = jdbc.query(QUERY,
//							new BookMapper());
//		} catch (DataAccessException e) {
//			return new ArrayList<>();
//		}
//		correctAuthors(books);
		return books;
	}

	@Override
	public Collection<Book> getByTitle(String title) {
		Map<String, String> params = Collections.singletonMap("title", title);
//        Collection<Book> books = null;
//		try {
//            books = jdbc.query(QUERY + "where b.title=:title",
//					params, new BookMapper());
//        } catch (DataAccessException e) {
		    return new ArrayList<>();
//        }
//		correctAuthors(books);
//		return books;
	}

	@Override
	public Book getByAuthorAndTitle(String author, String title) {
//        Map<String, String> params = new HashMap<>();
//        params.put("author_name", author);
//        params.put("title", title);
//		try {
//			Book book = jdbc.queryForObject(QUERY + "where b.title=:title and a.name=:author_name",
//					params, new BookMapper());
//			return correctAuthors(Collections.singleton(book)).stream().findFirst().orElse(null);
//		} catch (DataAccessException e) {
			return null;
//		}
	}

	@Override
	public Book getById(UUID id) {
//		Map<String, UUID> params = Collections.singletonMap("id", id);
//		try {
//			Book book = jdbc.queryForObject(QUERY + "where b.id=:id",
//					params, new BookMapper());
//			return correctAuthors(Collections.singleton(book)).stream().findFirst().orElse(null);
//		} catch (DataAccessException e) {
			return null;
//		}
	}

	@Override
	public Collection<Book> getByAuthor(String author) {
		Map<String, String> params = Collections.singletonMap("author", author);
//		Collection<Book> books = null;
//		try {
//			books = jdbc.query(QUERY + "where a.name=:author",
//					params, new BookMapper());
//		} catch (DataAccessException e) {
			return new ArrayList<>();
//		}
//		correctAuthors(books);
//		return books;
	}

	@Override
	public Collection<Book> getByGenre(String genre) {
		Map<String, String> params = Collections.singletonMap("genre", genre);
//		Collection<Book> books = null;
//		try {
//			books = jdbc.query(QUERY + "where g.name=:genre",
//					params, new BookMapper());
//		} catch (DataAccessException e) {
			return new ArrayList<>();
//		}
//		correctAuthors(books);
//		return books;
	}

	@Override
	@Transactional
	public void save(Book book) {
//		Map<String, String> params = new HashMap<>();
//		params.put("id", book.getId().toString());
//		params.put("title", book.getTitle());
//		if (book.getGenre() != null) {
//			String genreId = book.getGenre().getId().toString();
//			params.put("genre", genreId);
//			jdbc.update("insert into BOOKS (id, title, genre)" + "values (:id, :title, :genre)", params);
//		} else {
//			jdbc.update("insert into BOOKS (id, title)" + "values (:id, :title)", params);
//		}
//
//		if (book.getAuthors() != null && !book.getAuthors().isEmpty()) {
//			List<Map<String, Object>> batchValues = new ArrayList<>(book.getAuthors().size());
//			for (Author author : book.getAuthors()) {
//				batchValues.add(
//						new MapSqlParameterSource("id", UUID.randomUUID()).addValue("book", book.getId()).addValue("author", author.getId()).getValues());
//			}
//
//			jdbc.batchUpdate("insert into BOOKS_AUTHORS (id, book, author) " +
//					"values (:id, :book, :author)", batchValues.toArray(new Map[book.getAuthors().size()]));
//		}

	}

	@Override
	@Transactional
	public int delete(Book book) {
//		List<Map<String, Object>> batchValues = new ArrayList<>(book.getAuthors().size());
//		for (Author author : book.getAuthors()) {
//			batchValues.add(
//					new MapSqlParameterSource("book", book.getId()).addValue("author", author.getId()).getValues());
//		}
//
//		jdbc.batchUpdate("delete from BOOKS_AUTHORS " +
//				"where book=:book " +
//				"and author=:author", batchValues.toArray(new Map[book.getAuthors().size()]));
//
//		Map<String, UUID> params = Collections.singletonMap("id", book.getId());
//		return jdbc.update("delete from BOOKS " +
//				"where id=:id", params);
		return 0;
	}

	@Override
	@Transactional
	public Book update(Book book) {
//		Map<String, String> params = new HashMap<>();
//		params.put("id", book.getId().toString());
//		params.put("title", book.getTitle());
//		String genreId = book.getGenre() != null ? book.getGenre().getId().toString() : "NULL";
//		params.put("genre", genreId);
//
//		Book old = getById(book.getId());
//
//		if (old.getAuthors() != null && !old.getAuthors().isEmpty()) {
//			List<Map<String, Object>> batchValues = new ArrayList<>(old.getAuthors().size());
//			for (Author author : old.getAuthors()) {
//				batchValues.add(
//						new MapSqlParameterSource("book", old.getId()).addValue("author", author.getId()).getValues());
//			}
//
//			jdbc.batchUpdate("delete from BOOKS_AUTHORS " +
//					"where book=:book " +
//					"and author=:author", batchValues.toArray(new Map[old.getAuthors().size()]));
//		}
//
//		if (book.getAuthors() != null && !book.getAuthors().isEmpty()) {
//			List<Map<String, Object>> batch = new ArrayList<>(book.getAuthors().size());
//			for (Author author : book.getAuthors()) {
//				batch.add(
//						new MapSqlParameterSource("id", UUID.randomUUID()).addValue("book", book.getId()).addValue("author", author.getId()).getValues());
//			}
//
//			jdbc.batchUpdate("insert into BOOKS_AUTHORS (id, book, author) " +
//					"values (:id, :book, :author)", batch.toArray(new Map[book.getAuthors().size()]));
//		}
//		if (book.getGenre() != null) {
//			String id = book.getGenre().getId().toString();
//			params.put("genre", id);
//			return jdbc.update("update BOOKS set title=:title, genre=:genre where id=:id", params);
//		} else {
//			return jdbc.update("update BOOKS set title=:title where id=:id", params);
//		}
		return null;
	}

	@Override
	@Transactional
	public int deleteAll() {
//		Collection<Book> all =  this.getAll();
//		if (all.isEmpty()) {
//			return 0;
//		}
//		List<UUID> ids = all.stream().map(Book::getId).collect(toList());
//		MapSqlParameterSource params = new MapSqlParameterSource();
//		params.addValue("ids", ids);
//		jdbc.update("delete from BOOKS_AUTHORS " +
//				"where book in (:ids) ", params);
//		return jdbc.update("delete from BOOKS", new HashMap<>());
		return 0;
	}

	private void setAuthors(Book book) {
		setAuthors(Collections.singletonList(book));
	}

	private void setAuthors(Collection<Book> books) {
//        List<UUID> ids = books.stream().map(Book::getId).collect(Collectors.toList());
//		MapSqlParameterSource params = new MapSqlParameterSource();
//		params.addValue("ids", ids);
//        List<Map<String, Object>> rows = jdbc.queryForList("select ba.book as book_id, ba.author as author_id, a.name as author_name, g.id as genre_id, g.name as genre_name " +
//				"from BOOKS_AUTHORS ba, AUTHORS a, GENRES g, GENRES_AUTHORS ga " +
//				"where ba.book in (:ids) " +
//				"and ba.author = a.id " +
//						"and ga.author=a.id " +
//						"and g.id=ga.genre",
//				params);
//        for (Map<String, Object> row : rows) {
//            UUID bookId = (UUID)row.get("book_id");
//            UUID authorId = (UUID)row.get("author_id");
//            String authorName = (String)row.get("author_name");
//            UUID genreId = (UUID)row.get("genre_id");
//			String genreName = (String)row.get("genre_name");
//
//			Genre genre = new Genre(genreId, genreName);
//            Book book = books.stream().filter(x -> x.getId().equals(bookId)).findAny().orElse(null);
//            if (book != null) {
//            	Author author = book.getAuthors().stream().filter(a -> a.getId().equals(authorId)).findAny().orElse(null);
//            	if (author == null) {
//					author = new Author(authorId, authorName);
//					book.addAuthor(author);
//				}
//            	author.addGenre(genre);
//            }
//        }
    }

//	private Collection<Book> correctAuthors(Collection<Book> booksList) {
//		Collection<Book> books = new HashSet<>();
//		booksList.stream().collect(groupingBy(Function.identity(), HashMap::new,
//				mapping(Book::getAuthors, toSet()))).forEach((k, v) -> {
//			Collection<Author> authors = new HashSet<>();
//			v.forEach(x -> {
//				if (x != null) {
//					authors.addAll(x);
//				}
//			});
//			k.addAuthors(authors);
//			books.add(k);
//		});
//		return books;
//	}
}
