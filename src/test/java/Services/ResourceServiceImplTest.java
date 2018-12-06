package Services;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import ru.otus.ResourceService.ResourceServiceImpl;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

import static junit.framework.TestCase.fail;
import static org.junit.Assert.assertFalse;

@PrepareForTest({Files.class})
public class ResourceServiceImplTest {
	private ResourceServiceImpl underTest;
	private static String QUESTION = "question";
	private static String CORRECT = "correct";
	private static String FIRST = "first";
	private static String SECOND = "second";

	@Before
	public void init() {
		underTest = new ResourceServiceImpl();
	}

	@Test
	public void testReadQuestions() {
		Map<String, List<String>> expected = new HashMap<>();

		String line = QUESTION + "," + CORRECT + "," + FIRST + "," + SECOND;

		expected.put(QUESTION, Arrays.asList(CORRECT, FIRST, SECOND));


		try {
			PowerMockito.mockStatic(Files.class);
			PowerMockito.when(Files.readAllLines(Mockito.any(Path.class))).thenReturn(Collections.singletonList(line));

			Map<String, List<String>> actual = underTest.readQuestions();
			assertFalse(actual != null);

		} catch (Exception e) {
			fail();
		}
	}
}