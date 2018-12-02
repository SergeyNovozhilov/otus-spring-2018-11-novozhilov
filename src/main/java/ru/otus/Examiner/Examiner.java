package ru.otus.Examiner;

import ru.otus.ConsoleService.ConsoleServiceImpl;
import ru.otus.ConsoleService.IConsoleService;
import ru.otus.ResourceService.IResourceService;
import ru.otus.ResourceService.ResourceServiceImpl;

public class Examiner {
	private IResourceService resourceService;
	private IConsoleService consoleService;

	public Examiner() {

		resourceService = new ResourceServiceImpl();
		consoleService = new ConsoleServiceImpl();
	}

	public void start() {

		System.out.println(resourceService.readQuestions());
		String name = consoleService.getName();
		System.out.println("name: " + name);
	}
}
