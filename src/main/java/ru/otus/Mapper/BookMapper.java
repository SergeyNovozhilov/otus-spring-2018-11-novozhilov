package ru.otus.Mapper;

import org.springframework.jdbc.core.RowMapper;
import ru.otus.Domain.Author;
import ru.otus.Domain.Book;
import ru.otus.Domain.Genre;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

public class BookMapper implements RowMapper<Book> {

	public static final String ID = "id";
	public static final String TITLE = "title";
	public static final String GENRE_ID = "genre_id";
	public static final String GENRE_NAME = "genre_name";

	@Override
	public Book mapRow(ResultSet rs, int rowNum) throws SQLException {
		UUID id = UUID.fromString(rs.getString(ID));
		String title = rs.getString(TITLE);
		UUID genreId = UUID.fromString(rs.getString(GENRE_ID));
		String genreName = rs.getString(GENRE_NAME);

		return new Book(id, title, new Genre(genreId, genreName));
	}
}
