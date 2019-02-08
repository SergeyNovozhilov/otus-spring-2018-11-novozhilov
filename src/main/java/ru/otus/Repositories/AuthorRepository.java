package ru.otus.Repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.otus.Domain.Author;

import java.util.List;
import java.util.UUID;

public interface AuthorRepository extends JpaRepository<Author, UUID> {

	@Query("select a, g.id as genre_id, g.name as genre_name from Author a left join a.books b left join Genre g on b.genre = g.id")
	List<Object[]> getAll();

	@Query("select a, g.id as genre_id, g.name as genre_name from Author a left join a.books b left join Genre g on b.genre = g.id where a.name = :name")
	List<Object[]> findByName(@Param("name") String name);

	@Query("select a, g.id as genre_id, g.name as genre_name from Author a join a.books b join Genre g on g.id=b.genre where a in (select a from Author a join a.books b join Genre g on g.id=b.genre where g.name=:genre)")
	List<Object[]> findByGenre(@Param("genre") String genre);

	@Query("select a, g.id as genre_id, g.name as genre_name from Author a join a.books b join Genre g on b.genre = g.id where b.title = :title")
	List<Object[]> findByBook(@Param("title") String book);

}
