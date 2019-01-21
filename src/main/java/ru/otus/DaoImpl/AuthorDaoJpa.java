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
import java.util.function.Function;

import static java.util.stream.Collectors.*;

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
		TypedQuery<Author> query = em.createQuery("select a from Author a where a.name = :name", Author.class);
		query.setParameter("name", name);
		return query.getSingleResult();
	}

	@Override
	public Author getById(UUID id) {
		return em.find(Author.class, id);
	}

	@Override
	public Collection<Author> getByBook(String book) {
		Map<String, String> params = Collections.singletonMap("book", book);
//		try {
//			Collection<Author> authors = jdbc.query(QUERY + "where b.title=:book",
//					params, new AuthorMapper());
//			return correctGenres(authors);
//		} catch (DataAccessException e) {
			return new ArrayList<>();
//		}
	}

	@Override
	public Collection<Author> getByGenre(String genre) {
//		Map<String, String> params = Collections.singletonMap("genre", genre);
//		try {
//			Collection<Author> authors = jdbc.query(QUERY + "where g.name=:genre",
//					params, new AuthorMapper());
//			correctGenres(authors);
//			return authors;
//		} catch (DataAccessException e) {
			return new ArrayList<>();
//		}
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

//	private Collection<Author> correctGenres(Collection<Author> authorsList) {
//		Collection<Author> authors = new HashSet<>();
//		authorsList.stream().collect(groupingBy(Function.identity(), HashMap::new,
//				mapping(Author::getGenres, toSet()))).forEach((k, v) -> {
//			Collection<Genre> genres = new HashSet<>();
//			v.forEach(x -> genres.addAll(x));
//			k.addGenres(genres);
//			authors.add(k);
//		});
//		return authors;
//	}
}
