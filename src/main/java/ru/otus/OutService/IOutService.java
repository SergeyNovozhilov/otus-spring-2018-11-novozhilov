package ru.otus.OutService;

import java.util.List;
import java.util.Map;

public interface IOutService {
	Map<String, String> getAnswers(Map<String, List<String>> questions);
	String getName();
}
