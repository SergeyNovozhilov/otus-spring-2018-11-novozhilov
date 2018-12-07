package Services;

import org.junit.Before;
import org.junit.Test;
import ru.otus.ResourceService.ResourceServiceImpl;

public class ResourceServiceImplTest {
	private ResourceServiceImpl underTest;
	private final String SEPARATOR = ",";
	private final String QUESTION = "question";
	private final String CORRECT = "correct";
	private final String FIRST = "first";
	private final String SECOND = "second";

	@Before
	public void init() {
		underTest = new ResourceServiceImpl();
	}

	@Test
	public void testReadQuestions() {
		// TBD
		// problem with mocking classLoader.getResourceAsStream(fileName)
	}
}