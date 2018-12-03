package ru.otus.ResourceService;

import lombok.Setter;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.lang3.StringUtils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ResourceServiceImpl implements IResourceService {
	@Setter
	private String fileName;

	public Map<String, List<String>> readQuestions() {
		Map<String, List<String>> questions = new HashMap<>();
		try {
			ClassLoader classLoader = getClass().getClassLoader();
			BufferedReader reader
					= new BufferedReader(new FileReader(new File(classLoader.getResource(fileName).getFile())));
			CSVParser csvParser = new CSVParser(reader,
					CSVFormat.DEFAULT.withHeader("Question", "Correct answer", "First answer", "Second answer")
							.withIgnoreHeaderCase().withTrim());
			csvParser.getRecords().stream().forEach(record -> {
				if(record.getRecordNumber() == 1L) {
					return;
				}
				String question = record.get("Question");
				String correct = record.get("Correct answer");
				if (StringUtils.isBlank(question) || StringUtils.isBlank(correct)) {
					return;
				}
				List<String> answers = new ArrayList<>();
				answers.add(0, correct);
				String first = record.get("First answer");
				if (!StringUtils.isBlank(first)) {
					answers.add(first);
				}
				String second = record.get("Second answer");
				if (!StringUtils.isBlank(second)) {
					answers.add(second);
				}
				questions.put(question, answers);
			});
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		return questions;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
}
