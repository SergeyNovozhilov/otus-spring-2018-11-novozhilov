package ru.otus.DaoImpl;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.Dao.AuthorDao;
import ru.otus.Domain.Author;
import ru.otus.Domain.Genre;
import ru.otus.Mapper.AuthorMapper;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import java.util.*;
import java.util.function.Function;

import static java.util.stream.Collectors.*;

@Repository
public class AuthorDaoJpa implements AuthorDao {
	@PersistenceContext
	private EntityManager em;

	public static final String QUERY = "select a.id, a.name, g.id as genre_id, g.name as genre_name from AUTHORS a LEFT JOIN BOOKS_AUTHORS ba on a.id=ba.author LEFT JOIN BOOKS b on ba.book=b.id LEFT JOIN GENRES g on g.id=b.genre ";

	@Override
	public Collection<Author> getAll() {
		TypedQuery<Author> query = em.createQuery("select a from AUTHOR a", Author.class);
		return query.getResultList();
	}

	@Override
	public Author getByName(String name) {
		TypedQuery<Author> query = em.createQuery("select a from AUTHOR a where a.name = :name", Author.class);
		query.setParameter("name", name);
		return query.getSingleResult();
	}

	@Override
	public Author getById(UUID id) {
		return em.find(Author.class, id);
//		TypedQuery<Author> query = em.createQuery("select a from AUTHOR a where a.id = :id", Author.class);
//		query.setParameter("id", id);
//		return query.getSingleResult();
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
	@Transactional
	public void save(Author author) {
		em.persist(author);
	}

	@Override
	@Transactional
	public int delete(Author author) {
		Query query = em.createQuery("delete from AUTHORS a where id=:id");
		query.setParameter("id", author.getId());
		return query.executeUpdate();
	}

	@Override
	@Transactional
	public int update(Author author) {
		return em.merge(author);
//		Map<String, String> params = new HashMap<>();
//		params.put("id", author.getId().toString());
//		params.put("name", author.getName());
//
//		return jdbc.update("update AUTHORS set name=:name where id=:id", params);
	}

	@Override
	@Transactional
	public int deleteAll() {
		Query query = em.createQuery("delete from AUTHORS a");
		return query.executeUpdate();
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
