package ru.otus.test.Service;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.junit4.SpringRunner;
import ru.otus.ResourceService.ResourceServiceImpl;
import ru.otus.Wrappers.ClassLoaderWrapper;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;
import static ru.otus.ResourceService.ResourceServiceImpl.SEPARATOR;

@RunWith(SpringRunner.class)
@ActiveProfiles("Test")
@SpringBootTest
public class ResourceServiceImplTest {

	@Mock
	private ClassLoaderWrapper wrapper;
	@InjectMocks
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
