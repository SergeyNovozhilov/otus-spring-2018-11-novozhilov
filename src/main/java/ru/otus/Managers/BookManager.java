package ru.otus.Managers;

import org.apache.commons.lang3.StringUtils;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.Dtos.AuthorDto;
import ru.otus.Dtos.BookDto;
import ru.otus.Dtos.CommentDto;
import ru.otus.Dtos.GenreDto;
import ru.otus.Entities.Author;
import ru.otus.Entities.Book;
import ru.otus.Entities.Genre;
import ru.otus.Exceptions.NotFoundException;
import ru.otus.Repositories.AuthorRepository;
import ru.otus.Repositories.BookRepository;
import ru.otus.Repositories.GenreRepository;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class BookManager implements Manager<BookDto> {
    private AuthorRepository authorRepository;
    private GenreRepository genreRepository;
    private BookRepository bookRepository;
    private ModelMapper modelMapper;

    public BookManager(AuthorRepository authorRepository, GenreRepository genreRepository, BookRepository bookRepository, ModelMapper modelMapper) {
        this.authorRepository = authorRepository;
        this.genreRepository = genreRepository;
        this.bookRepository = bookRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public BookDto create(String title) {
        Book book = new Book(title);
        return modelMapper.map(bookRepository.save(book), BookDto.class);
    }

    public BookDto addGenre(BookDto book, String genreName) {
        Genre genre = genreRepository.findByName(genreName);
        if (genre == null) {
            genre = new Genre(genreName);
            genreRepository.save(genre);
        }
        book.setGenre(modelMapper.map(genre, GenreDto.class));

        return modelMapper.map(bookRepository.save(modelMapper.map(book, Book.class)), BookDto.class);
    }

    public BookDto addAuthors(BookDto book, List<String> authors) {
        authors.forEach(a -> {
            Author author = authorRepository.findByName(a);
            if (author == null) {
                author = new Author(a);
                authorRepository.save(author);
            }
            book.addAuthor(modelMapper.map(author, AuthorDto.class));
        });

        return modelMapper.map(bookRepository.save(modelMapper.map(book, Book.class)), BookDto.class);
    }

    public BookDto addComment(BookDto book, String comment) {
        book.addComment(new CommentDto(comment));

        return modelMapper.map(bookRepository.save(modelMapper.map(book, Book.class)), BookDto.class);
    }

    public BookDto removeComment(BookDto book, String comment) {
        CommentDto commentObj = book.getComments().stream().filter(c -> StringUtils.equalsIgnoreCase(c.getComment(), comment)).findAny().orElse(null);
        book.getComments().remove(commentObj);

        return modelMapper.map(bookRepository.save(modelMapper.map(book, Book.class)), BookDto.class);
    }

    @Override
    public Collection<BookDto> get(String title, String genre, String author) throws NotFoundException {
        Collection<Book> books = new ArrayList<>();
        Collection<Book> booksByTitle = null;
        Collection<Book> booksByGenre = null;
        Collection<Book> booksByAuthor = null;

        if (StringUtils.isBlank(title) && StringUtils.isBlank(genre) && StringUtils.isBlank(author)) {
            return returnBooks(bookRepository.findAll());
        }

        if (StringUtils.isNotBlank(title) && StringUtils.isNotBlank(author) && StringUtils.isBlank(genre)) {
            booksByTitle = bookRepository.findByTitle(title);
            booksByTitle.forEach(b -> {
                if (b.getAuthors().stream().filter(a -> a.getName().equals(author)).findAny().orElse(null) != null) {
                    books.add(b);
                }
            });
            return returnBooks(books);
        }

        if (StringUtils.isNotBlank(title)) {
            booksByTitle = bookRepository.findByTitle(title);
            if (!booksByTitle.isEmpty() && books.isEmpty()) {
                books.addAll(booksByTitle);
            }
        }
        if (StringUtils.isNotBlank(genre)) {
            booksByGenre = bookRepository.findByGenre(genre);
            if (!booksByGenre.isEmpty() && books.isEmpty()) {
                books.addAll(booksByGenre);
            }
        }
        if (StringUtils.isNotBlank(author)) {
            booksByAuthor = bookRepository.findByAuthor(author);
            if (!booksByAuthor.isEmpty() && books.isEmpty()) {
                books.addAll(booksByAuthor);
            }
        }

        if (booksByTitle != null) {
            books.retainAll(booksByTitle);
        }
        if (booksByGenre != null) {
            books.retainAll(booksByGenre);
        }
        if (booksByAuthor != null) {
            books.retainAll(booksByAuthor);
        }

        return returnBooks(books);
    }

    @Override
    public BookDto update(BookDto book) {
        return modelMapper.map(bookRepository.save(modelMapper.map(book, Book.class)), BookDto.class);
    }

    @Override
    public void delete(BookDto book) {
        bookRepository.delete(modelMapper.map(book, Book.class));
    }

    private Collection<BookDto> returnBooks(Collection<Book> books) throws NotFoundException{
        if (books == null || books.isEmpty()) {
            throw new NotFoundException("No Books were found.");
        }
        return books.stream().map(b -> modelMapper.map(b, BookDto.class)).collect(Collectors.toSet());
    }
}
