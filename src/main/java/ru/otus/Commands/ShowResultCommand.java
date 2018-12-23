package ru.otus.Commands;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;
import ru.otus.Domain.Result;
import ru.otus.Domain.ResultDao;

import java.util.List;

@ShellComponent
public class ShowResultCommand {

	private ResultDao storage;

	@ShellMethod("Show examination results")
	public void showResults(@ShellOption(defaultValue="")String name) {
		if (StringUtils.isNotBlank(name)) {
			printResults(name);
		} else {
			storage.getAllNames().stream().forEach(this::printResults);
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
	public void setStorage(ResultDao storage) {
		this.storage = storage;
	}
}
