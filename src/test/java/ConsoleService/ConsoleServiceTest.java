package ConsoleService;

import org.junit.Test;
import ru.otus.OutService.ConsoleService;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ConsoleServiceTest {
	private ConsoleService underTest;
	@Test
	public void testGetName() {
		underTest = mock(ConsoleService.class);
		when(underTest.getName()).thenReturn("some name");

		assertEquals(underTest.getName(), "some name");
	}

}
