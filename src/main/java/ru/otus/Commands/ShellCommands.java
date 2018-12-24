package ru.otus.Commands;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;
import ru.otus.Config.ApplicationProps;
import ru.otus.Domain.Result;
import ru.otus.Domain.ResultDao;
import ru.otus.Examiner.Examiner;

import java.util.List;

@ShellComponent
public class ShellCommands {

	private Examiner ex;
	private ResultDao storage;
	private ApplicationProps applicationProps;

	@ShellMethod("Start the examination")
	public void startExam() {
		ex.start();
	}

	@ShellMethod("Show examination results")
	public void showResults(@ShellOption(defaultValue="")String name) {
		if (StringUtils.isNotBlank(name)) {
			printResults(name);
		} else {
			storage.getAllNames().stream().forEach(this::printResults);
		}
	}

	@ShellMethod("Change language")
	public void changeLang(String lang) {
		List<String> langs = applicationProps.getLanguages();
		if (langs.contains(lang)) {
			applicationProps.changeLanguage(lang);
			System.out.println("Language has been changed to " + lang);
		} else {
			System.out.println("Unknown language: " + lang);
			System.out.println("Possible languages : " + langs.toString());
		}
	}

	private void printResults(String name) {
		System.out.println("\n" + name + " results:");
		System.out.println("----------------");
		List<Result> results = storage.get(name);
		for (int i = 0; i < results.size(); i++) {
			Result result = results.get(i);
			System.out.println((i + 1) + ") " + result.getCorrect() + " " + result.getResultString() + " " + result.getTotal());
		}
	}

	@Autowired
	public void setApplicationProps(ApplicationProps applicationProps) {
		this.applicationProps = applicationProps;
	}

	@Autowired
	public void setStorage(ResultDao storage) {
		this.storage = storage;
	}

	@Autowired
	public void setEx(Examiner ex) {
		this.ex = ex;
	}
}
