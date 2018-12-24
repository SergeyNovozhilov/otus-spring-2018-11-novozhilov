package ru.otus;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import ru.otus.Config.ApplicationProps;

@SpringBootApplication
@EnableConfigurationProperties(ApplicationProps.class)
public class App {

	public static void main(String[] args) {
		SpringApplication.run(App.class, args);
	}
}
