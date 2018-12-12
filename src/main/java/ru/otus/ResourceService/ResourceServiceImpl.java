package ru.otus.ResourceService;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import ru.otus.Wrappers.ClassLoaderWrapper;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ResourceServiceImpl implements IResourceService {

	private String fileName;
	private ClassLoaderWrapper classLoaderWrapper;

	public static final String SEPARATOR = ";";

	private final int CORRECT_ANSWER_INDEX = 0;
	private final int FIELDS_MIN_NUMBER = 2;
	private final int QUESTION = 0;
	private final int CORRECT_ANSWER = 1;

	@Autowired
	public void setClassLoaderWrapper(ClassLoaderWrapper classLoaderWrapper) {
		this.classLoaderWrapper = classLoaderWrapper;
	}

	public Map<String, List<String>> readQuestions() {
		Map<String, List<String>> questions = new HashMap<>();

		try (InputStream is = classLoaderWrapper.getResourceAsStream(this.getClass(), fileName);
			 InputStreamReader isr = new InputStreamReader(is);
			 BufferedReader br = new BufferedReader(isr)){
			String line;
			while ((line = br.readLine()) != null)
			{
				String[] record = line.split(SEPARATOR);
				if (record.length < FIELDS_MIN_NUMBER  || StringUtils.isBlank(record[QUESTION]) || StringUtils.isBlank(record[CORRECT_ANSWER])) {
					continue;
				}
				List<String> answers = new ArrayList<>();
				answers.add(CORRECT_ANSWER_INDEX, record[CORRECT_ANSWER]);
				for (int i = CORRECT_ANSWER + 1; i < record.length; i++)
				if (!StringUtils.isBlank(record[i])) {
					answers.add(record[i]);
				}
				questions.put(record[QUESTION], answers);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		return questions;
	}

	@Override
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
}