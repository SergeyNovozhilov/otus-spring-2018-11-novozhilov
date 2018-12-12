package ru.otus.test.Examiner;

import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import ru.otus.Examiner.Examiner;
import ru.otus.OutService.ConsoleService;
import ru.otus.ResourceService.ResourceServiceImpl;

@Profile("test")
@Configuration
public class ExaminerTestConfiguration {
	@Autowired
	private ConsoleService consoleService;
	@Autowired
	private ResourceServiceImpl resourceService;

	@Bean
	@Primary
	public ResourceServiceImpl resourceService() {
		return Mockito.mock(ResourceServiceImpl.class);
	}

	@Bean
	@Primary
	public ConsoleService consoleService() {
		return Mockito.mock(ConsoleService.class);
	}

	@Bean
	@Primary
	public MessageSource messageSource() {
		return Mockito.mock(MessageSource.class);
	}

	@Bean
	@Primary
	public Examiner examiner() {
		return new Examiner(resourceService, consoleService);
	}
}
