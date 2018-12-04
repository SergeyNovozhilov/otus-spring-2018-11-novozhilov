package ru.otus.ResourceService;

import lombok.Setter;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.lang3.StringUtils;
import ru.otus.Utils.Headers;

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

	private static int CORRECT_ANSWER_INDEX = 0;

	public Map<String, List<String>> readQuestions() {
		Map<String, List<String>> questions = new HashMap<>();
		try {
			ClassLoader classLoader = getClass().getClassLoader();
			BufferedReader reader
					= new BufferedReader(new FileReader(new File(classLoader.getResource(fileName).getFile())));
			CSVParser csvParser = new CSVParser(reader,
					CSVFormat.DEFAULT.withHeader(Headers.class)
					.withFirstRecordAsHeader().withIgnoreHeaderCase().withTrim());
			csvParser.getRecords().stream().forEach(record -> {
				if (StringUtils.isBlank(record.get(Headers.Question)) || StringUtils.isBlank(record.get(Headers.Correct))) {
					return;
				}
				List<String> answers = new ArrayList<>();
				answers.add(CORRECT_ANSWER_INDEX, record.get(Headers.Correct));
				if (!StringUtils.isBlank(record.get(Headers.First))) {
					answers.add(record.get(Headers.First));
				}
				if (!StringUtils.isBlank(record.get(Headers.Second))) {
					answers.add(record.get(Headers.Second));
				}
				questions.put(record.get(Headers.Question), answers);
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
