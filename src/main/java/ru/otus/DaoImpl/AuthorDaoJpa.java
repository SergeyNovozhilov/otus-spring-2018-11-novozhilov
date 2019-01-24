package ru.otus.DaoImpl;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.Dao.AuthorDao;
import ru.otus.Domain.Author;
import ru.otus.Domain.Genre;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import java.util.*;

@Repository
public class AuthorDaoJpa implements AuthorDao {
	@PersistenceContext
	private EntityManager em;

	@Override
	public Collection<Author> getAll() {
		TypedQuery<Author> query = em.createQuery("select a from Author a", Author.class);
		return query.getResultList();
	}

	@Override
	public Author getByName(String name) {
		try {
			TypedQuery<Author> query = em.createQuery("select a from Author a where a.name = :name", Author.class);
			query.setParameter("name", name);
			return query.getSingleResult();
		} catch (Exception e) {
			return null;
		}
	}

	@Override
	public Author getById(UUID id) {
		return em.find(Author.class, id);
	}

	@Override
	public Collection<Author> getByBook(String book) {
		TypedQuery<Author> query = em.createQuery("SELECT a FROM Author a JOIN a.books b where b.title = :title", Author.class);
		query.setParameter("title", book);
		return query.getResultList();
	}

	@Override
	public Collection<Author> getByGenre(String genre) {
		Map<UUID, Author> authorMap = new HashMap<>();
		Query query = em.createQuery("select a, g.id as genre_id, g.name as genre_name from Author a JOIN a.books b JOIN Genre g on b.genre = g.id where g.name = :genre");
		query.setParameter("genre", genre);

		List<Object[]> results =query.getResultList();

		for (Object[] result : results) {
			Genre g = new Genre((UUID)result[1], (String)result[2]);
			Author a = (Author) result[0];
			Author author = authorMap.get(a.getId());
			if (author != null) {
				author.getGenres().add(g);
			} else {
				a.getGenres().add(g);
				authorMap.put(a.getId(), a);
			}
		}

		return authorMap.values();
	}

	@Override
	@Transactional
	public void save(Author author) {
		em.persist(author);
	}

	@Override
	@Transactional
	public void delete(Author author) {
		em.remove(author);
	}

	@Override
	@Transactional
	public Author update(Author author) {
		return em.merge(author);
	}

	@Override
	@Transactional
	public int deleteAll() {
		Query query = em.createQuery("delete from Author a");
		return query.executeUpdate();
	}
}
