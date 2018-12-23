package ru.otus.test.Service;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.shell.jline.InteractiveShellApplicationRunner;
import org.springframework.shell.jline.ScriptShellApplicationRunner;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.junit4.SpringRunner;
import ru.otus.ResourceService.ResourceService;
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
@SpringBootTest(properties = {
		InteractiveShellApplicationRunner.SPRING_SHELL_INTERACTIVE_ENABLED + "=false",
		ScriptShellApplicationRunner.SPRING_SHELL_SCRIPT_ENABLED + "=false"
})
public class ResourceServiceImplTest {
	private final String QUESTION = "question";
	private final String CORRECT = "correct";
	private final String FIRST = "first";
	private final String SECOND = "second";

	@MockBean
	private ClassLoaderWrapper wrapper;
	@Autowired
	private ResourceService underTest;

	@Configuration
	class ConsoleServiceTestConfiguration {

		@Bean
		public ResourceService resourceService() {
			return new ResourceServiceImpl();
		}
	}

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
