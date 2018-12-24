package ru.otus.test.Service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.shell.jline.InteractiveShellApplicationRunner;
import org.springframework.shell.jline.ScriptShellApplicationRunner;
import org.springframework.test.context.junit4.SpringRunner;
import ru.otus.OutService.ConsoleService;
import ru.otus.Scanner.Scanner;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@SpringBootTest(properties = {
		InteractiveShellApplicationRunner.SPRING_SHELL_INTERACTIVE_ENABLED + "=false",
		ScriptShellApplicationRunner.SPRING_SHELL_SCRIPT_ENABLED + "=false"
})
public class ConsoleServiceTest {
	@MockBean
	private Scanner scanner;

	@Configuration
	class ConsoleServiceTestConfiguration {
		@Autowired
		private Scanner scanner;

		@Bean
		public ConsoleService consoleService() {
			return new ConsoleService(scanner);
		}
	}

	@Autowired
	private ConsoleService underTest;

	@Test
	public void testGetName() {
		System.out.println("testGetName()");
		String expected = "name";
		when(scanner.getInput()).thenReturn(expected);
		String actual = underTest.getName();
		assertEquals(actual, expected);
	}

	@Test
	public void testGetAnswers() {
		System.out.println("testGetAnswers()");
		String expectedQuestion = "Question";
		String expectedAnswer = "Answer";
		Map<String, List<String>> questions = new HashMap<>();
		questions.put(expectedQuestion, Collections.singletonList(expectedAnswer));
		when(scanner.getInput()).thenReturn("1");
		Map<String, String> answers = underTest.getAnswers(questions);
		assertNotNull(answers);
		assertTrue(answers.size() == 1);
		assertTrue(answers.containsKey(expectedQuestion));
		assertFalse(answers.values().isEmpty());
		assertEquals(answers.get(expectedQuestion), expectedAnswer);
	}
}
