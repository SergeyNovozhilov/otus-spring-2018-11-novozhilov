package ru.otus.Mapper;

import org.springframework.jdbc.core.RowMapper;
import ru.otus.Domain.Author;
import ru.otus.Domain.Book;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

public class AuthorMapper implements RowMapper<Author> {
	public static final String ID = "id";
	public static final String NAME = "name";
	@Override
	public Author mapRow(ResultSet rs, int rowNum) throws SQLException {
		UUID id = UUID.fromString(rs.getString(ID));
		String name = rs.getString(NAME);

		return new Author(id, name);
	}
}
