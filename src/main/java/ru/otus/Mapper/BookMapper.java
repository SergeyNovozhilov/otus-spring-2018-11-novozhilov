package ru.otus.Mapper;

import org.apache.commons.lang3.StringUtils;
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
	public static final String AUTHOR_ID = "author_id";
	public static final String AUTHOR_NAME = "author_name";

	@Override
	public Book mapRow(ResultSet rs, int rowNum) throws SQLException {
		Author author = null;
		Genre genre = null;
		UUID id = UUID.fromString(rs.getString(ID));
		String title = rs.getString(TITLE);
		String genreIdStr = rs.getString(GENRE_ID);
		String genreName = rs.getString(GENRE_NAME);
		if (StringUtils.isNotBlank(genreIdStr) && StringUtils.isNotBlank(genreName)) {
			genre = new Genre(UUID.fromString(genreIdStr), genreName);
		}
		Book book = new Book(id, title, genre);
		String authorIdStr = rs.getString(AUTHOR_ID);
		String authorName = rs.getString(AUTHOR_NAME);
		if (StringUtils.isNotBlank(authorIdStr) && StringUtils.isNotBlank(authorName)) {
			author = new Author(UUID.fromString(authorIdStr), authorName);
			book.addAuthor(author);
		}

		return book;
	}
}
