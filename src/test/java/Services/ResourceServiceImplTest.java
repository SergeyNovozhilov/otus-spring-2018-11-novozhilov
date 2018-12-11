package Services;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import ru.otus.ResourceService.ResourceServiceImpl;
import ru.otus.Wrappers.ClassLoaderWrapper;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;
import static ru.otus.ResourceService.ResourceServiceImpl.SEPARATOR;

@ActiveProfiles("test")
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = AppTest.class)
public class ResourceServiceImplTest {

	@Autowired
	private ClassLoaderWrapper wrapper;
	@Autowired
	private ResourceServiceImpl underTest;

	private final String QUESTION = "question";
	private final String CORRECT = "correct";
	private final String FIRST = "first";
	private final String SECOND = "second";

	@Before
	public void init() {
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
		assertEquals(actual, expected);
	}
}
