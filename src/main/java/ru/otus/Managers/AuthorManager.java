package ru.otus.Managers;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import ru.otus.Domain.Author;
import ru.otus.Domain.Genre;
import ru.otus.Exceptions.NotFoundException;
import ru.otus.Repositories.AuthorRepository;

import java.util.*;

@Service
public class AuthorManager implements Manager<Author> {
    public static final int AUTHOR = 0;
    public static final int GENRE_ID = 1;
    public static final int GENRE_NAME = 2;

    private AuthorRepository authorRepository;

    public AuthorManager(AuthorRepository authorRepository) {
        this.authorRepository = authorRepository;
    }

    @Override
    public Author create(String name) {
        Author author = new Author(name);
        authorRepository.save(author);
        return author;
    }

    @Override
    public Collection<Author> get(String name, String genre, String book) throws NotFoundException {
        Collection<Author> authors = new ArrayList<>();
        if (StringUtils.isBlank(name) && StringUtils.isBlank(genre) && StringUtils.isBlank(book)) {
            authors.addAll(authorRepository.findAll());
            if (authors == null || authors.isEmpty()) {
                throw new NotFoundException("No Authors not found");
            }
            return authors;
        } else {
            if (StringUtils.isNotBlank(name)) {
                Author author = getAuthors(authorRepository.findByName(name)).stream().findAny().orElse(null);
                if (author == null) {
                    throw new NotFoundException("Author with name: " + name + " not found");
                }
                authors.add(author);
            } else if (StringUtils.isNotBlank(book)) {
                authors.addAll(getAuthors(authorRepository.findByBook(book)));
                if (authors.isEmpty()) {
                    throw new NotFoundException("Authors of book: " + book + " not found");
                }
            } else if (StringUtils.isNotBlank(genre)) {
                authors.addAll(getAuthors(authorRepository.findByGenre(genre)));
                if (authors.isEmpty()) {
                    throw new NotFoundException("No authors with genre: " + genre);
                }
            }
        }
        return authors;
    }

    @Override
    public Author update(Author author) {
        return authorRepository.save(author);
    }

    @Override
    public void delete(Author author) {
        authorRepository.delete(author);
    }

    public Collection<Author> getAuthors(List<Object[]> results) {
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
