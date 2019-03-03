package ru.otus.Repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.otus.Entities.Author;

import java.util.Collection;
import java.util.UUID;

public interface AuthorRepository extends JpaRepository<Author, UUID> {

	Author findByName(String name);

	@Query("select distinct a from Author a join a.books b join Genre g on g.id=b.genre where a in (select a from Author a join a.books b join Genre g on g.id=b.genre where g.name=:genre)")
	Collection<Author> findByGenre(@Param("genre") String genre);

	@Query("select a from Author a join a.books b where b.title = :title")
	Collection<Author> findByBook(@Param("title") String book);

}
