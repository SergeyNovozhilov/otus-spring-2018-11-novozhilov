package ru.otus.Managers;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.entities.Genre;
import ru.otus.Exceptions.NotFoundException;

import java.util.ArrayList;
import java.util.Collection;

@Service
@Transactional
public class GenreManager implements Manager<Genre> {
    private GenreRepository genreRepository;;

    public GenreManager(GenreRepository genreRepository) {
        this.genreRepository = genreRepository;
    }

    @Override
    public Genre create(String name) {
        Genre genre = new Genre(name);

        return genreRepository.save(genre);
    }

    @Override
    public Collection<Genre> get(String name, String book, String author) throws NotFoundException {
        Collection<Genre> genres = new ArrayList<>();
        if (StringUtils.isBlank(name) && StringUtils.isBlank(author) && StringUtils.isBlank(book)) {
            return genreRepository.findAll();
        } else {
            if (StringUtils.isNotBlank(name)) {
                Genre genre = genreRepository.findByName(name);
                if (genre == null) {
                    throw new NotFoundException("Genre with name: " + name + " not found.");
                }
                genres.add(genre);
            } else if (StringUtils.isNotBlank(book)) {
                Genre genre = genreRepository.findByBook(book);
                if (genre == null) {
                    throw new NotFoundException("Genre of book: " + name + " not found");
                }
                genres.add(genre);
            } else if (StringUtils.isNotBlank(author)) {
                genres.addAll(genreRepository.findByAuthor(author));
                if (genres.isEmpty()) {
                    throw new NotFoundException("Genre of author: " + author + " not found");
                }
            }
        }
        return genres;
    }

    @Override
    public Genre update(Genre genre) {
        return genreRepository.save(genre);
    }

    @Override
    public void delete(Genre genre) {
        genreRepository.delete(genre);
    }
}
