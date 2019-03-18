package ru.otus.Managers;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.Dtos.AuthorDto;
import ru.otus.Entities.Author;
import org.modelmapper.ModelMapper;
import ru.otus.Exceptions.NotFoundException;
import ru.otus.Repositories.AuthorRepository;

import java.util.ArrayList;
import java.util.Collection;
import java.util.stream.Collectors;

@Service
@Transactional
public class AuthorManager implements Manager<AuthorDto> {

    private AuthorRepository authorRepository;
    private ModelMapper modelMapper;

    public AuthorManager(AuthorRepository authorRepository, ModelMapper modelMapper) {
        this.authorRepository = authorRepository;
        this.modelMapper= modelMapper;
    }

    @Override
    public AuthorDto create(String name) {
        Author author = new Author(name);
        authorRepository.save(author);
        return modelMapper.map(author, AuthorDto.class);
    }

    @Override
    public Collection<AuthorDto> get(String name, String genre, String book) throws NotFoundException {
        Collection<Author> authors = new ArrayList<>();
        if (StringUtils.isBlank(name) && StringUtils.isBlank(genre) && StringUtils.isBlank(book)) {
            authors.addAll(authorRepository.findAll());
            if (authors == null || authors.isEmpty()) {
                throw new NotFoundException("No Authors not found");
            }
            return authors.stream().map(a -> modelMapper.map(a, AuthorDto.class)).collect(Collectors.toList());
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
        return authors.stream().map(a -> modelMapper.map(a, AuthorDto.class)).collect(Collectors.toList());
    }

    @Override
    public AuthorDto update(AuthorDto author) {
        return modelMapper.map(authorRepository.save(modelMapper.map(author, Author.class)), AuthorDto.class);
    }

    @Override
    public void delete(AuthorDto author) {
        authorRepository.delete(modelMapper.map(author, Author.class));
    }
}
