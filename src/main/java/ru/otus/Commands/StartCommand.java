package ru.otus.Commands;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import ru.otus.Examiner.Examiner;

@ShellComponent
public class StartCommand {

	private Examiner ex;

	@ShellMethod("Start the examination")
	public void startExam() {
		ex.start();
	}

	@Autowired
	public void setEx(Examiner ex) {
		this.ex = ex;
	}
}
