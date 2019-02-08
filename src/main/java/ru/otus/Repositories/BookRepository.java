package ru.otus.Repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.otus.Domain.Book;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

public interface BookRepository extends JpaRepository<Book, UUID> {

	@Override
	@Query("select distinct b from Book b left join fetch b.comments c")
	List<Book> findAll();

	@Query("select distinct b from Book b left join fetch b.comments c where b.title = : title")
	Collection<Book> findByTitle(@Param("title") String title);

	@Query("select distinct b FROM Book b JOIN b.authors a left join fetch b.comments c where a.name = :author")
	Collection<Book> findByAuthor(@Param("author") String author);

	@Query("SELECT distinct b FROM Book b left join fetch b.comments c JOIN b.genre g where g.name = :genre")
	Collection<Book> findByGenre(@Param("genre") String genre);
}
