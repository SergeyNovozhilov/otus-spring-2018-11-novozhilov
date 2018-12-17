package ru.otus.Examiner;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.otus.Config.ApplicationProps;
import ru.otus.Domain.Result;
import ru.otus.OutService.OutService;
import ru.otus.ResourceService.ResourceService;
import org.springframework.context.MessageSource;

import java.util.*;

@Component
public class Examiner {
	private ResourceService resourceService;
	private OutService outService;

	private ApplicationProps properties;

	private MessageSource messageSource;

	public Examiner(ResourceService resourceService, OutService outService) {
		this.resourceService = resourceService;
		this.outService = outService;
	}

	public void start() {
		Result result = new Result();
		String localeStr = "en_US";
		if (StringUtils.isNotBlank(properties.getLocale())) {
			localeStr = properties.getLocale();
		}
		Locale locale = new Locale(localeStr);
		result.setResultString(messageSource.getMessage("result.string", null, locale));

		resourceService.setFileName("questions_" + localeStr.split("_")[0] + ".csv");
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

	@Autowired
	public void setProperties(ApplicationProps properties) {
		this.properties = properties;
	}

	@Autowired
	public void setMessageSource(MessageSource messageSource) {
		this.messageSource = messageSource;
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
