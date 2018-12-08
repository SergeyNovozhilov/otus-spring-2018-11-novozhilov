package Services;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import ru.otus.ResourceService.ResourceServiceImpl;
import ru.otus.Wrappers.ClassLoaderWrapper;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertTrue;
import static ru.otus.ResourceService.ResourceServiceImpl.SEPARATOR;

import static org.mockito.Mockito.when;

public class ResourceServiceImplTest {
	private ResourceServiceImpl underTest;
	private ClassLoaderWrapper wrapper;
	private final String QUESTION = "question";
	private final String CORRECT = "correct";
	private final String FIRST = "first";
	private final String SECOND = "second";

	@Before
	public void init() {
		wrapper = Mockito.mock(ClassLoaderWrapper.class);
		underTest = new ResourceServiceImpl();
		underTest.setClassLoaderWrapper(wrapper);
		underTest.setFileName("");
	}

	@Test
	public void testReadQuestions() {
		String line = QUESTION + SEPARATOR + CORRECT + SEPARATOR + FIRST + SEPARATOR + SECOND;
		Map<String, List<String>> expected = new HashMap<>();
		expected.put(QUESTION, Arrays.asList(CORRECT, FIRST, SECOND));
		InputStream is = new ByteArrayInputStream(line.getBytes());
		when(wrapper.getResourceAsStream(Mockito.any(), Mockito.anyString())).thenReturn(is);

		Map<String, List<String>> actual = underTest.readQuestions();
		assertTrue(actual != null);
	}
}