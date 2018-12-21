package ru.otus.Commands;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import ru.otus.Domain.ResultDao;

@ShellComponent
public class ShowResultCommand {

	private ResultDao storage;

	@ShellMethod("Get examination results")
	public void getResults(String name) {
		storage.get(name).stream().forEach(r ->
			System.out.println(r.getResult())
		);
	}

	@Autowired
	public void setStorage(ResultDao storage) {
		this.storage = storage;
	}
}
