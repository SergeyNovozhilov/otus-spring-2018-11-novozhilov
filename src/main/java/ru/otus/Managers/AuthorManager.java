package ru.otus.managers;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import ru.otus.entities.Author;
import ru.otus.exceptions.NotFoundException;
import ru.otus.repositories.AuthorRepository;

import java.util.ArrayList;
import java.util.Collection;

@Service
public class AuthorManager implements Manager<Author> {

    private AuthorRepository authorRepository;

    public AuthorManager(AuthorRepository authorRepository) {
        this.authorRepository = authorRepository;
    }

    @Override
    public Author create(String name) {
        Author author = new Author(name);

        return authorRepository.save(author);
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
                Author author = authorRepository.findByName(name);
                if (author == null) {
                    throw new NotFoundException("Author with name: " + name + " not found");
                }
                authors.add(author);
            } else if (StringUtils.isNotBlank(book)) {
                authors.addAll(authorRepository.findByBook(book));
                if (authors.isEmpty()) {
                    throw new NotFoundException("Authors of book: " + book + " not found");
                }
            } else if (StringUtils.isNotBlank(genre)) {
                authors.addAll(authorRepository.findByGenre(genre));
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
}
