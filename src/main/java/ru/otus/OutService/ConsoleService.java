package ru.otus.OutService;

import org.springframework.stereotype.Service;
import ru.otus.Scanner.Scanner;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ConsoleService implements OutService {

	private Scanner scanner;
	private String askName;
	private String askQuestions;

	public ConsoleService(Scanner scanner) {
		this.scanner = scanner;
	}

	@Override
	public Map<String, String> getAnswers(Map<String, List<String>> questions) {
		Map<String, String> answers = new HashMap<>();
		System.out.println(askQuestions);
		questions.forEach((k, v) -> {
			System.out.println(k);
			boolean gotAnswer = false;
			while (!gotAnswer) {
				gotAnswer = true;
				for (int i = 0; i < v.size(); i++) {
					System.out.println((i + 1) + ") " + v.get(i));
				}
				String answer = scanner.getInput();
				int number = -1;
				try {
					number = Integer.valueOf(answer);
					if (number <= 0 || number - 1 >= v.size()) {
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
		System.out.println(askName);
		username = scanner.getInput();

		return username;
	}

	@Override
    public void setAskName(String askName) {
        this.askName = askName;
    }

    @Override
    public void setAskQuestions(String askQuestions) {
        this.askQuestions = askQuestions;
    }
}
