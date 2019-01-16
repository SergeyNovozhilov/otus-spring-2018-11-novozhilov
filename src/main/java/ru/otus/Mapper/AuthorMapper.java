package ru.otus.Mapper;

import org.apache.commons.lang3.StringUtils;
import org.springframework.jdbc.core.RowMapper;
import ru.otus.Domain.Author;
import ru.otus.Domain.Book;
import ru.otus.Domain.Genre;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

public class AuthorMapper implements RowMapper<Author> {
	public static final String ID = "id";
	public static final String NAME = "name";
	public static final String GENRE_ID = "genre_id";
	public static final String GENRE_NAME = "genre_name";
	@Override
	public Author mapRow(ResultSet rs, int rowNum) throws SQLException {
		UUID id = UUID.fromString(rs.getString(ID));
		String name = rs.getString(NAME);
		String uuid = rs.getString(GENRE_ID);
		String genreName = rs.getString(GENRE_NAME);
		Genre genre = null;
		if (StringUtils.isNotBlank(uuid) && StringUtils.isNotBlank(genreName)) {
			UUID genreId = UUID.fromString(uuid);
			genre = new Genre(genreId, genreName);
		}

		Author author = new Author(id, name);
		author.addGenre(genre);
		return author;
	}
}
