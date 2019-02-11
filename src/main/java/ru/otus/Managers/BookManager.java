package ru.otus.Managers;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import ru.otus.Domain.Author;
import ru.otus.Domain.Book;
import ru.otus.Domain.Comment;
import ru.otus.Domain.Genre;
import ru.otus.Exceptions.NotFoundException;
import ru.otus.Repositories.AuthorRepository;
import ru.otus.Repositories.BookRepository;
import ru.otus.Repositories.GenreRepository;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Service
public class BookManager implements Manager<Book> {
    private AuthorRepository authorRepository;
    private GenreRepository genreRepository;
    private BookRepository bookRepository;

    public BookManager(AuthorRepository authorRepository, GenreRepository genreRepository, BookRepository bookRepository) {
        this.authorRepository = authorRepository;
        this.genreRepository = genreRepository;
        this.bookRepository = bookRepository;
    }

    @Override
    public Book create(String title) {
        Book book = new Book(title);
        return bookRepository.save(book);
    }

    public Book addGenre(Book book, String genreName) {
        Genre genre = genreRepository.findByName(genreName);
        if (genre == null) {
            genre = new Genre(genreName);
            genreRepository.save(genre);
        }
        book.setGenre(genre);

        return bookRepository.save(book);
    }

    public Book addAuthors(Book book, List<String> authors) {
        authors.forEach(a -> {
            Author author = null;
            if (authorRepository.findByName(a) == null) {
                author = new Author(a);
                authorRepository.save(author);
            }
            book.addAuthor(author);
        });

        return bookRepository.save(book);
    }

    public Book addComment(Book book, String comment) {
        book.addComment(new Comment(comment));

        return bookRepository.save(book);
    }

    public Book removeComment(Book book, String comment) {
        Comment commentObj = book.getComments().stream().filter(c -> StringUtils.equalsIgnoreCase(c.getComment(), comment)).findAny().orElse(null);
        book.getComments().remove(commentObj);

        return bookRepository.save(book);
    }

    @Override
    public Collection<Book> get(String title, String genre, String author) throws NotFoundException {
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
    public Book update(Book book) {
        return bookRepository.save(book);
    }

    @Override
    public void delete(Book book) {
        bookRepository.delete(book);
    }

    private Collection<Book> returnBooks(Collection<Book> books) throws NotFoundException{
        if (books == null || books.isEmpty()) {
            throw new NotFoundException("No Books were found.");
        }
        return books;
    }
}
