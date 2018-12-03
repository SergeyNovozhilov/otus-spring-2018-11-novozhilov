package ru.otus.Examiner;

import org.apache.commons.lang3.StringUtils;
import ru.otus.ConsoleService.ConsoleServiceImpl;
import ru.otus.ConsoleService.IConsoleService;
import ru.otus.Domain.Result;
import ru.otus.ResourceService.IResourceService;
import ru.otus.ResourceService.ResourceServiceImpl;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Examiner {
	private IResourceService resourceService;
	private IConsoleService consoleService;

	public Examiner() {

		resourceService = new ResourceServiceImpl();
		consoleService = new ConsoleServiceImpl();
	}

	public void start() {
		Result result = new Result();
		Map<String, List<String>> questions = resourceService.readQuestions();
		String name = consoleService.getName();
		result.setName(name);
		result.setScore(getScore(prepareQuestions(questions), consoleService.getAnswers(questions)));
		System.out.println(result.getResult());
	}

	private String getScore(Map<String, String> questionAnswer, Map<String, String> answers) {
		int score = 0;
		for(Map.Entry entry: answers.entrySet()) {
			if (StringUtils.equals(questionAnswer.get(entry.getKey()), (String)entry.getValue())) {
				score++;
			}
		}
		return score + " correct answers from " + answers.entrySet().size();
	}

	private Map<String, String> prepareQuestions(Map<String, List<String>> questions) {
		Map<String, String> questionAnswer = new HashMap<>();
		questions.forEach((k, v) -> {
			questionAnswer.put(k, v.get(0));
			Collections.shuffle(v);
		});
		return questionAnswer;
	}
}
