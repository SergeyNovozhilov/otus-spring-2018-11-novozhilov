package ru.otus;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import org.h2.tools.Console;

import java.sql.SQLException;

@SpringBootApplication
//@EnableConfigurationProperties(ApplicationProps.class)
public class App {

	public static void main(String[] args) throws SQLException {
		SpringApplication.run(App.class, args);

		Console.main(args);
	}
}
