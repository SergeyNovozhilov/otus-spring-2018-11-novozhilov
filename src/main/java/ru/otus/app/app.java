package ru.otus.app;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import ru.otus.Examiner.Examiner;

public class app {
	public static void main(String[] args) {
		ApplicationContext ctx = new ClassPathXmlApplicationContext("spring-config.xml");
		Examiner ex = ctx.getBean(Examiner.class);
		ex.start();
	}
}
