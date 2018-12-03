package ru.otus.ConsoleService;

import java.util.List;
import java.util.Map;

public interface IConsoleService {
	Map<String, String> getAnswers(Map<String, List<String>> questions);
	String getName();
}
