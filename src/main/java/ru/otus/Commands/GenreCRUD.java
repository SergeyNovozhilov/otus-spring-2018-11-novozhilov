package ru.otus.Commands;

import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;

@ShellComponent
public class GenreCRUD {

	@ShellMethod("Create genre")
	public void createGenre() {
		System.out.println("Create genre");
	}
}
