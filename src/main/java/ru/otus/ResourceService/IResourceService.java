package ru.otus.ResourceService;

import java.util.List;
import java.util.Map;

public interface IResourceService {
	Map<String, List<String>> readQuestions();
}
