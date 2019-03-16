package ru.otus.Managers;

import org.apache.commons.lang3.StringUtils;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import ru.otus.Dtos.GenreDto;
import ru.otus.Entities.Genre;
import ru.otus.Exceptions.NotFoundException;
import ru.otus.Repositories.GenreRepository;

import java.util.ArrayList;
import java.util.Collection;
import java.util.stream.Collectors;

@Service
public class GenreManager implements Manager<GenreDto> {
    private GenreRepository genreRepository;
    private ModelMapper modelMapper;

    public GenreManager(GenreRepository genreRepository, ModelMapper modelMapper) {
        this.genreRepository = genreRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public GenreDto create(String name) {
        Genre genre = new Genre(name);
        genreRepository.save(genre);
        return modelMapper.map(genre, GenreDto.class);
    }

    @Override
    public Collection<GenreDto> get(String name, String book, String author) throws NotFoundException {
        Collection<GenreDto> genres = new ArrayList<>();
        if (StringUtils.isBlank(name) && StringUtils.isBlank(author) && StringUtils.isBlank(book)) {
            return genreRepository.findAll().stream().map(g -> modelMapper.map(g, GenreDto.class)).collect(Collectors.toSet());
        } else {
            if (StringUtils.isNotBlank(name)) {
                GenreDto genre = modelMapper.map(genreRepository.findByName(name), GenreDto.class);
                if (genre == null) {
                    throw new NotFoundException("Genre with name: " + name + " not found.");
                }
                genres.add(genre);
            } else if (StringUtils.isNotBlank(book)) {
                GenreDto genre = modelMapper.map(genreRepository.findByBook(book), GenreDto.class);
                if (genre == null) {
                    throw new NotFoundException("Genre of book: " + name + " not found");
                }
                genres.add(genre);
            } else if (StringUtils.isNotBlank(author)) {
                genres.addAll(genreRepository.findByAuthor(author).stream().map(g -> modelMapper.map(g, GenreDto.class)).collect(Collectors.toSet()));
                if (genres.isEmpty()) {
                    throw new NotFoundException("Genre of author: " + author + " not found");
                }
            }
        }
        return genres;
    }

    @Override
    public GenreDto update(GenreDto genre) {
        return modelMapper.map(genreRepository.save(modelMapper.map(genre, Genre.class)), GenreDto.class);
    }

    @Override
    public void delete(GenreDto genre) {
        genreRepository.delete(modelMapper.map(genre, Genre.class));
    }
}
