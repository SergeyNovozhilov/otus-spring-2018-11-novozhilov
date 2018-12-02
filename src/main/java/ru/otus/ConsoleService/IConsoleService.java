package ru.otus.ConsoleService;

import java.util.List;
import java.util.Map;

public interface IConsoleService {
	void setQuestions(Map<String, List<String>> questions);
	Map<String, String> getAnswers();
	String getName();
}
