package ru.otus.DaoImpl;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.stereotype.Repository;
import ru.otus.Dao.GenreDao;
import ru.otus.Domain.Author;
import ru.otus.Domain.Genre;
import ru.otus.Mapper.GenreMapper;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import java.util.*;

@Repository
public class GenreDaoJpa implements GenreDao {
	@PersistenceContext
	private EntityManager em;

	@Override
	public Collection<Genre> getAll() {
		TypedQuery<Genre> query = em.createQuery("select g from Genre g", Genre.class);
		return query.getResultList();
	}

	@Override
	public Genre getByName(String name) {
		TypedQuery<Genre> query = em.createQuery("select g from Genre g where g.name = :name", Genre.class);
		query.setParameter("name", name);
		return query.getSingleResult();
	}

	@Override
	public Genre getById(UUID id) {
		return em.find(Genre.class, id);
	}

	@Override
	public Collection<Genre> getByAuthor(String author) {
//		Map<String, String> params = Collections.singletonMap("name", author);
//		try {
//			return jdbc.query("select g.id, g.name " +
//							"from AUTHORS a, GENRES g, BOOKS b, BOOKS_AUTHORS ba " +
//							"where a.name=:name " +
//							"and a.id=ba.author " +
//							"and b.id=ba.book " +
//							"and g.id=b.genre",
//					params, new GenreMapper());
//		} catch (DataAccessException e) {
			return new ArrayList<>();
//		}
	}

	@Override
	public Genre getByBook(String book) {
		TypedQuery<Genre> query = em.createQuery("select g from Genre g LEFT JOIN Book b on b.genre=g.id where b.title = :title", Genre.class);
		query.setParameter("title", book);
		return query.getSingleResult();
	}

	@Override
	public void save(Genre genre) {
		em.persist(genre);
	}

	@Override
	public void delete(Genre genre) {
		em.remove(genre);
	}

	@Override
	public Genre update(Genre genre) {
		return em.merge(genre);
	}

	@Override
	public int deleteAll() {
		Query query = em.createQuery("delete from Author a");
		return query.executeUpdate();
	}
}
