package ru.otus.ResourceService;

import lombok.Setter;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ResourceServiceImpl implements IResourceService {
	@Setter
	private String fileName;

	private final String SEPARATOR = ",";

	private final int CORRECT_ANSWER_INDEX = 0;
	private final int QUESTION = 0;
	private final int CORRECT_ANSWER = 1;
	private final int FIRST_ANSWER = 2;
	private final int SECOND_ANSWER = 3;

	public Map<String, List<String>> readQuestions() {
		Map<String, List<String>> questions = new HashMap<>();
		try {
			List<String> lines = Files.readAllLines(Paths.get(this.getClass().getResource("/" + fileName).toURI()));

			lines.stream().map(line -> line.split(SEPARATOR)).forEach(record -> {
				if (StringUtils.isBlank(record[QUESTION]) || StringUtils.isBlank(record[CORRECT_ANSWER])) {
					return;
				}
				List<String> answers = new ArrayList<>();
				answers.add(CORRECT_ANSWER_INDEX, record[CORRECT_ANSWER]);
				if (!StringUtils.isBlank(record[FIRST_ANSWER])) {
					answers.add(record[FIRST_ANSWER]);
				}
				if (!StringUtils.isBlank(record[SECOND_ANSWER])) {
					answers.add(record[SECOND_ANSWER]);
				}
				questions.put(record[QUESTION], answers);
			});
		} catch (IOException e1) {
			e1.printStackTrace();
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}
		return questions;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
}