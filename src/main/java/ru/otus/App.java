package ru.otus;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Profile;
import ru.otus.Examiner.Examiner;

@SpringBootApplication
@Profile("!Test")
public class App implements CommandLineRunner {

	private Examiner ex;

	public static void main(String[] args) {
		SpringApplication.run(App.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		ex.start();
	}

	@Autowired
	public void setEx(Examiner ex) {
		this.ex = ex;
	}
}
