package ru.otus.OutService;

import java.util.List;
import java.util.Map;

public interface OutService {
	Map<String, String> getAnswers(Map<String, List<String>> questions);
	String getName();
	void setAskName(String askName);
	void setAskQuestions(String askQuestions);
}
