package ru.otus.app;

import org.springframework.context.ApplicationContext;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.*;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.context.support.ResourceBundleMessageSource;
import ru.otus.Examiner.Examiner;

import java.util.Locale;

@Configuration
@ComponentScan(basePackages = "ru.otus")
//@PropertySource("classpath:application.properties")
public class App {
	public static void main(String[] args) {
		ApplicationContext ctx = new AnnotationConfigApplicationContext(App.class);
		Examiner ex = ctx.getBean(Examiner.class);
		ex.setLocale(new Locale("ru_RU"));

		ex.start();
	}

//	@Bean
//	public static PropertySourcesPlaceholderConfigurer propertySourcesPlaceholderConfigurer() {
//		return new PropertySourcesPlaceholderConfigurer();
//	}

	@Bean
	public MessageSource messageSource () {
		ResourceBundleMessageSource messageSource = new ResourceBundleMessageSource();
		messageSource.setBasename("i18n/bundle");
		messageSource.setDefaultEncoding("UTF-8");
		return messageSource;
	}
}
