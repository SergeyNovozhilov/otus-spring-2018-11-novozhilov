package Services;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import ru.otus.OutService.ConsoleService;
import ru.otus.Scanner.ScannerImpl;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;
import static org.mockito.Mockito.when;


@RunWith(MockitoJUnitRunner.class)
public class ConsoleServiceTest {
	@Mock
	private ScannerImpl scanner;
	@InjectMocks
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
