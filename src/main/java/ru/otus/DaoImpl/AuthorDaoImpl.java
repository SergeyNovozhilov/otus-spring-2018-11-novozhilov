package ru.otus.DaoImpl;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.Dao.AuthorDao;
import ru.otus.Domain.Author;
import ru.otus.Domain.Genre;

import javax.persistence.*;
import java.util.*;

@Repository
public class AuthorDaoImpl implements AuthorDao {
    public static final int AUTHOR = 0;
    public static final int GENRE_ID = 1;
    public static final int GENRE_NAME = 2;

    @PersistenceContext
    private EntityManager em;

    @Override
    public Collection<Author> getAll() {
//        TypedQuery<Author> query = em.createQuery("select a from Author a", Author.class);
//        return query.getResultList();
        try {
            Query query = em.createQuery(
                    "select a, g.id as genre_id, g.name as genre_name from Author a left join a.books b left join Genre g on b.genre = g.id");
            return getAuthors(query.getResultList());
        } catch (NoResultException e) {
            return  Collections.EMPTY_SET;
        }
    }

    @Override
    public Author getByName(String name) {
        try {
            Query query = em.createQuery("select a, g.id as genre_id, g.name as genre_name from Author a left join a.books b left join Genre g on b.genre = g.id where a.name = :name");
            query.setParameter("name", name);
            Optional<Author> a = getAuthors(query.getResultList()).stream().findFirst();
            return a.orElse(null);
        } catch (NoResultException e) {
            return  null;
        }
    }

    @Override
    public Author getById(UUID id) {
        return em.find(Author.class, id);
    }

    @Override
    public Collection<Author> getByBook(String book) {
        try {
            Query query = em.createQuery("select a, g.id as genre_id, g.name as genre_name from Author a join a.books b join Genre g on b.genre = g.id where b.title = :title");
            query.setParameter("title", book);
            return getAuthors(query.getResultList());
        } catch (NoResultException e) {
            return  Collections.EMPTY_SET;
        }
    }

    @Override
    public Collection<Author> getByGenre(String genre) {
        try {
            Query query = em.createQuery("select a, g.id as genre_id, g.name as genre_name from Author a join a.books b join Genre g on b.genre = g.id where g.name = :genre");
            query.setParameter("genre", genre);
            return getAuthors(query.getResultList());
        } catch (NoResultException e) {
            return  Collections.EMPTY_SET;
        }
    }

    @Override
    @Transactional
    public Author save(Author author) {
        em.persist(author);
        em.flush();
        return author;
    }

    @Override
    @Transactional
    public void delete(Author author) {
        em.remove(em.contains(author) ? author : em.merge(author));
    }

    @Override
    @Transactional
    public Author update(Author author) {
        return em.merge(author);
    }

    private Collection<Author> getAuthors (List<Object[]> results) {
        Map<UUID, Author> authorMap = new HashMap<>();

        for (Object[] result : results) {
            Genre g = new Genre((UUID) result[GENRE_ID], (String) result[GENRE_NAME]);
            Author a = (Author) result[AUTHOR];
            Author author = authorMap.get(a.getId());
            if (author == null) {
                authorMap.put(a.getId(), a);
                author = a;
            }
            if (g.getName() != null && g.getId() != null) {
                author.getGenres().add(g);
            }
        }

        return new HashSet<>(authorMap.values());
    }

}
