package ru.otus.ResourceService;

import java.util.List;
import java.util.Map;

public interface ResourceService {
	Map<String, List<String>> readQuestions();
	void setFileName(String fileName);
}
