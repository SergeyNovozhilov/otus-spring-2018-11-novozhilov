package ru.otus.Examiner;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cglib.core.Local;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;
import ru.otus.Domain.Result;
import ru.otus.OutService.IOutService;
import ru.otus.ResourceService.IResourceService;

import java.util.*;

@Component
public class Examiner {
	private IResourceService resourceService;
	private IOutService outService;
	@Autowired
	private MessageSource messageSource;

	@Value("${locale}")
	private String localeString = "en_US";

	public Examiner(IResourceService resourceService, IOutService outService) {
		this.resourceService = resourceService;
		this.outService = outService;
	}

	public void start() {
		Result result = new Result();
		Locale locale = new Locale(localeString);
		result.setResultString(messageSource.getMessage("result.string", null, locale));
		resourceService.setFileName(messageSource.getMessage("file.name", null, locale));
		outService.setAskName(messageSource.getMessage("ask.name", null, locale));
		outService.setAskQuestions(messageSource.getMessage("ask.questions", null, locale));
		Map<String, List<String>> questions = resourceService.readQuestions();
		Map<String, String> questionsAnswers = prepareQuestions(questions);
		String name = outService.getName();
		Map<String, String> answers = outService.getAnswers(questions);
		result.setName(name);
		result.setCorrect(getCorrectAnswers(questionsAnswers, answers));
		result.setTotal(answers.size());
		System.out.println(result.getResult());
	}

	private int getCorrectAnswers(Map<String, String> questionAnswer, Map<String, String> answers) {
		int score = 0;
		for(Map.Entry entry: answers.entrySet()) {
			if (StringUtils.equals(questionAnswer.get(entry.getKey()), (String)entry.getValue())) {
				score++;
			}
		}
		return score;
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
