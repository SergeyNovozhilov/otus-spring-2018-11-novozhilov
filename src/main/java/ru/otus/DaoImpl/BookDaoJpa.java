package ru.otus.DaoImpl;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.Dao.BookDao;
import ru.otus.Domain.Book;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import java.util.*;

@Repository
public class BookDaoJpa implements BookDao {

	@PersistenceContext
	private EntityManager em;

	@Override
	public Collection<Book> getAll() {
		try {
			TypedQuery<Book> query = em.createQuery("select b from Book b", Book.class);
			return query.getResultList();
		} catch (Exception e) {
			return Collections.EMPTY_SET;
		}
	}

	@Override
	public Collection<Book> getByTitle(String title) {
		try {
			TypedQuery<Book> query = em.createQuery("select b from Book b where b.title = :title", Book.class);
			query.setParameter("title", title);
			return query.getResultList();
		} catch (Exception e) {
			return Collections.EMPTY_SET;
		}
	}

	@Override
	public Book getById(UUID id) {
		try {
			return em.find(Book.class, id);
		} catch (Exception e) {
			return null;
		}
	}

	@Override
	public Collection<Book> getByAuthor(String author) {
		try {
			TypedQuery<Book> query = em.createQuery("SELECT b FROM Book b JOIN b.authors a where a.name = :author", Book.class);
			query.setParameter("author", author);
			return query.getResultList();
		} catch (Exception e) {
			return Collections.EMPTY_SET;
		}
	}

	@Override
	public Collection<Book> getByGenre(String genre) {
		try {
			TypedQuery<Book> query = em.createQuery("SELECT b FROM Book b JOIN b.genre g where g.name = :genre", Book.class);
			query.setParameter("genre", genre);
			return query.getResultList();
		} catch (Exception e) {
			return Collections.EMPTY_SET;
		}
	}

	@Override
	@Transactional
	public void save(Book book) {
		em.persist(book);
		em.flush();
	}

	@Override
	@Transactional
	public void delete(Book book) {
		Query query = em.createQuery("delete from Book b where b.id = : id");
		query.setParameter("id", book.getId());
		int res = query.executeUpdate();
		em.flush();
	}

	@Override
	@Transactional
	public Book update(Book book) {
		if (em.contains(book)) {
			em.merge(book);
			em.flush();
		}
		return null;
	}

	@Override
	@Transactional
	public int deleteAll() {
		Query query = em.createQuery("delete from Book b");
		return query.executeUpdate();
	}
}