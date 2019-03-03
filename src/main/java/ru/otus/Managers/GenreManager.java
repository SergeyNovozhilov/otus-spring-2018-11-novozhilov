package ru.otus.Managers;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import ru.otus.Dtos.GenreDto;
import ru.otus.Entities.Genre;
import ru.otus.Exceptions.NotFoundException;
import ru.otus.Repositories.GenreRepository;

import java.util.ArrayList;
import java.util.Collection;

@Service
public class GenreManager implements Manager<GenreDto> {
    private GenreRepository genreRepository;

    public GenreManager(GenreRepository genreRepository) {
        this.genreRepository = genreRepository;
    }

    @Override
    public GenreDto create(String name) {
        Genre genre = new Genre(name);
        genreRepository.save(genre);
        return genre;
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
                    throw new NotFoundException("GenreDto with name: " + name + " not found.");
                }
                genres.add(genre);
            } else if (StringUtils.isNotBlank(book)) {
                Genre genre = genreRepository.findByBook(book);
                if (genre == null) {
                    throw new NotFoundException("GenreDto of book: " + name + " not found");
                }
                genres.add(genre);
            } else if (StringUtils.isNotBlank(author)) {
                genres.addAll(genreRepository.findByAuthor(author));
                if (genres.isEmpty()) {
                    throw new NotFoundException("GenreDto of author: " + author + " not found");
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
