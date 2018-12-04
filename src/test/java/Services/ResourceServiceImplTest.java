package Services;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import ru.otus.ResourceService.ResourceServiceImpl;
import ru.otus.Utils.Headers;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.util.*;

import static junit.framework.TestCase.fail;
import static org.junit.Assert.assertFalse;

//@RunWith(PowerMockRunner.class)
//@PrepareForTest(value = { ResourceServiceImpl.class })
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

		expected.put(QUESTION, Arrays.asList(CORRECT, FIRST, SECOND));

		ClassLoader classLoader = Mockito.mock(ClassLoader.class);
		URL url = PowerMockito.mock(URL.class);
		File file = PowerMockito.mock(File.class);
		FileReader fileReader = Mockito.mock(FileReader.class);
		BufferedReader bufferedReader = Mockito.mock(BufferedReader.class);
		CSVParser csvParser = PowerMockito.mock(CSVParser.class);
		CSVRecord record = PowerMockito.mock(CSVRecord.class);

		try {
//			PowerMockito.whenNew(URL.class).withParameterTypes(String.class).withArguments(Mockito.anyString()).thenReturn(url);
			Mockito.when(classLoader.getResource(Mockito.anyString())).thenReturn(url);
			PowerMockito.whenNew(File.class).withParameterTypes(URL.class).withArguments(url).thenReturn(file);

			PowerMockito.whenNew(FileReader.class).withArguments(file).thenReturn(fileReader);
			PowerMockito.whenNew(BufferedReader.class).withArguments(fileReader).thenReturn(bufferedReader);
			PowerMockito.whenNew(CSVParser.class).withArguments(bufferedReader,
					CSVFormat.DEFAULT.withHeader(Headers.class)
							.withFirstRecordAsHeader().withIgnoreHeaderCase().withTrim()).thenReturn(csvParser);
		} catch (Exception e) {
			fail();
		}

		try {
			Mockito.when(csvParser.getRecords()).thenReturn(Collections.singletonList(record));
		} catch (IOException e) {
			fail();
		}
		Mockito.when(record.get(Headers.Question)).thenReturn(QUESTION);
		Mockito.when(record.get(Headers.Correct)).thenReturn(CORRECT);
		Mockito.when(record.get(Headers.First)).thenReturn(FIRST);
		Mockito.when(record.get(Headers.Second)).thenReturn(SECOND);

		Map<String, List<String>> actual = underTest.readQuestions();

		assertFalse(actual == null);
	}
}
