package ru.otus.ResourceService;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class ResourceServiceImpl implements IResourceService {

	public String readQuestions() {
		String fileName = "questions.csv";
		String out = "";
		//		ClassLoader classLoader = getClass().getClassLoader();
		//		File file = new File(classLoader.getResource(fileName).getFile());
		//		if(file.exists()) {
		//			System.out.println("exists");
		//			try {
		//				out = FileUtils.readFileToString(file);
		//			} catch (IOException e) {
		//				e.printStackTrace();
		//			}
		//		} else {
		//			System.out.println("not exists");
		//		}
		//		return out;


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
				System.out.println("question: " + record.get("Question"));
				System.out.println("correct: " + record.get("Correct answer"));
			});
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		return out;

	}

}
