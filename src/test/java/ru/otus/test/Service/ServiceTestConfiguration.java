package ru.otus.test.Service;

import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import ru.otus.OutService.ConsoleService;
import ru.otus.ResourceService.ResourceServiceImpl;
import ru.otus.Scanner.ScannerImpl;
import ru.otus.Wrappers.ClassLoaderWrapper;

@Profile("test")
@Configuration
public class ServiceTestConfiguration {
	@Autowired
	private ScannerImpl scanner;

	@Bean
	@Primary
	public ClassLoaderWrapper classLoaderWrapper() {
		return Mockito.mock(ClassLoaderWrapper.class);
	}

	@Bean
	@Primary
	public ScannerImpl scanner() {
		return Mockito.mock(ScannerImpl.class);
	}

	@Bean
	@Primary
	public ResourceServiceImpl resourceService() {
		return new ResourceServiceImpl();
	}

	@Bean
	@Primary
	public ConsoleService consoleService() {
		return new ConsoleService(scanner);
	}
}
