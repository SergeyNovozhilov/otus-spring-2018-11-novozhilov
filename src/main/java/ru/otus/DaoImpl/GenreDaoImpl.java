package ru.otus.DaoImpl;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.Dao.GenreDao;
import ru.otus.Domain.Genre;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.Collection;
import java.util.UUID;

@Repository
public class GenreDaoImpl implements GenreDao {
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
        TypedQuery<Genre> query = em.createQuery("SELECT b.genre FROM Book b JOIN b.authors a where a.name = :name", Genre.class);
        query.setParameter("name", author);
        return query.getResultList();
    }

    @Override
    public Genre getByBook(String book) {
        TypedQuery<Genre> query = em.createQuery("select g from Genre g JOIN Book b on b.genre=g.id where b.title = :title", Genre.class);
        query.setParameter("title", book);
        return query.getSingleResult();
    }

    @Override
    @Transactional
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
}
