package ru.otus.Mapper;

import org.springframework.jdbc.core.RowMapper;
import ru.otus.Domain.Author;
import ru.otus.Domain.Book;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

public class BookMapper implements RowMapper<Book> {

	public static final String ID = "id";
	public static final String TITLE = "title";
	public static final String AUTHOR_ID = "author_id";
	public static final String AUTHOR_NAME = "author_name";

	@Override
	public Book mapRow(ResultSet rs, int rowNum) throws SQLException {
		UUID id = UUID.fromString(rs.getString(ID));
		String title = rs.getString(TITLE);
		UUID authorId = UUID.fromString(rs.getString(AUTHOR_ID));
		String authorName = rs.getString(AUTHOR_NAME);

		return new Author(id, );
	}
}
