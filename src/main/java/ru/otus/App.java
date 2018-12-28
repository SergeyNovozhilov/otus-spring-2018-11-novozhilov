package ru.otus;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
//@EnableConfigurationProperties(ApplicationProps.class)
public class App {

	public static void main(String[] args) {
		SpringApplication.run(App.class, args);
	}
}
