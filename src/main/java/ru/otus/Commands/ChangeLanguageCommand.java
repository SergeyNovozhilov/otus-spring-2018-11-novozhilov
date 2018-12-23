package ru.otus.Commands;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import ru.otus.Config.ApplicationProps;

import java.util.List;

@ShellComponent
public class ChangeLanguageCommand {

	private ApplicationProps applicationProps;

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

	@Autowired
	public void setApplicationProps(ApplicationProps applicationProps) {
		this.applicationProps = applicationProps;
	}
}
