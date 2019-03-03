package ru.otus.Repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.otus.Entities.Genre;

import java.util.Collection;
import java.util.UUID;

public interface GenreRepository extends JpaRepository<Genre, UUID> {
	Genre findByName(String name);

	@Query("select g from Genre g JOIN Book b on b.genre=g.id where b.title = :title")
	Genre findByBook(@Param("title") String title);

	@Query("select b.genre from Book b join b.authors a where a.name = :name")
	Collection<Genre> findByAuthor(@Param("name") String author);
}
