package ru.otus.test.Examiner;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import ru.otus.OutService.ConsoleService;
import ru.otus.ResourceService.ResourceServiceImpl;

@ActiveProfiles("test")
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = ExaminerTestConfiguration.class)
public class ExaminerTest {
	@Autowired
	private ConsoleService consoleService;
	@Autowired
	private ResourceServiceImpl resourceService;

	@Test
	public void test() {

	}
}
