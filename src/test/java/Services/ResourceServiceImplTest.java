package Services;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import ru.otus.ResourceService.ResourceServiceImpl;

import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

import static junit.framework.TestCase.fail;
import static org.junit.Assert.assertFalse;

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

	}
}