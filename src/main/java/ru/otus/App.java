package ru.otus;

import org.h2.tools.Console;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import ru.otus.Dao.GenreDao;
import ru.otus.Domain.Genre;

import java.sql.SQLException;

@SpringBootApplication
public class App {

	public static void main(String[] args) throws SQLException {
		ApplicationContext context = SpringApplication.run(App.class, args);
		GenreDao genreDao = context.getBean(GenreDao.class);
		genreDao.save(new Genre("Genre"));

		Genre g = genreDao.getByName("Genre");
		System.out.println(g.getName());
		Console.main(args);
	}
}
