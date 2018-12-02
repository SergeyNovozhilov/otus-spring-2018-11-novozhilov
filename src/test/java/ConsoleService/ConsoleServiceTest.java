package ConsoleService;

import org.junit.Test;
import ru.otus.ConsoleService.ConsoleServiceImpl;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ConsoleServiceTest {
	private ConsoleServiceImpl underTest;
	@Test
	public void testGetName() {
		underTest = mock(ConsoleServiceImpl.class);
		when(underTest.getName()).thenReturn("some name");

		assertEquals(underTest.getName(), "some name");
	}

}
