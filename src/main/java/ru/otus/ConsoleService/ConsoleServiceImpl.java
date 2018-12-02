package ru.otus.ConsoleService;

import lombok.Setter;
import ru.otus.Scanner.IScanner;
import ru.otus.Scanner.ScannerImpl;

import java.util.*;

public class ConsoleServiceImpl implements IConsoleService {

	private IScanner scanner = new ScannerImpl();

	@Setter
	private Map<String, List<String>> questions;

	@Override
	public Map<String, String> getAnswers() {
		Map<String, String> answers = new HashMap<>();
		System.out.println("Please answer the questions: ");
		questions.forEach((k, v) -> {
			Collections.shuffle(v);
			boolean gotAnswer = false;
			while (!gotAnswer) {
				gotAnswer = true;
				for (int i = 0; i < v.size(); i++) {
					System.out.println((i + 1) + ") " + v.get(i));
				}
				String answer = scanner.getInput();
				int number = -1;
				try {
					number = Integer.getInteger(answer);
					if (number <= 0 || number >= v.size()) {
						gotAnswer = false;
					}
				} catch (Exception e) {
					gotAnswer = false;
				}
				if (gotAnswer) {
					answers.put(k, v.get(number - 1));
				}
			}
		});

		return answers;
	}

	@Override
	public String getName() {
		String username;
		System.out.println("Enter your full name: ");
		username = scanner.getInput();

		return username;
	}
}
