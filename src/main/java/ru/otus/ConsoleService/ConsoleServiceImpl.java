package ru.otus.ConsoleService;

import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class ConsoleServiceImpl implements IConsoleService {
	@Override public void setQuestions(Map<String, List<String>> questions) {

	}

	@Override public Map<String, String> getAnswers() {
		return null;
	}

	@Override public String getName() {
		String username = "";
		System.out.println("Enter your full name: ");
		Scanner scanner = new Scanner(System.in);
		username = scanner.nextLine();

		return username;
	}
}
