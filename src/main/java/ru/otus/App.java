package ru.otus;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Profile;
import ru.otus.Config.ApplicationProps;

@SpringBootApplication
@EnableConfigurationProperties(ApplicationProps.class)
@Profile("!Test")
public class App {

	public static void main(String[] args) {
		SpringApplication.run(App.class, args);
	}
}
