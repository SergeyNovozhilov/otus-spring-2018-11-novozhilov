package ru.otus.ResourceService;

import org.apache.commons.lang3.StringUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ResourceServiceImpl implements IResourceService {
	private String fileName;

	private final String SEPARATOR = ",";

	private final int CORRECT_ANSWER_INDEX = 0;
	private final int QUESTION = 0;
	private final int CORRECT_ANSWER = 1;
	private final int FIRST_ANSWER = 2;
	private final int SECOND_ANSWER = 3;

	public Map<String, List<String>> readQuestions() {
		Map<String, List<String>> questions = new HashMap<>();
		ClassLoader classLoader = getClass().getClassLoader();

		try (InputStream is = classLoader.getResourceAsStream(fileName);
			 InputStreamReader isr = new InputStreamReader(is);
			 BufferedReader br = new BufferedReader(isr)){
			String line;
			while ((line = br.readLine()) != null)
			{
				String[] record = line.split(SEPARATOR);
				if (StringUtils.isBlank(record[QUESTION]) || StringUtils.isBlank(record[CORRECT_ANSWER])) {
					continue;
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
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		return questions;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
}