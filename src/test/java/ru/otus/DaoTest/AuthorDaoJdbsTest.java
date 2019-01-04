package ru.otus.DaoTest;

import org.h2.tools.Console;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import ru.otus.Dao.AuthorDao;
import ru.otus.Domain.Author;

import java.sql.SQLException;
import java.util.Collection;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertTrue;

@RunWith(SpringRunner.class)
@SpringBootTest
public class AuthorDaoJdbsTest {
    @Autowired
    private AuthorDao authorDao;

    @Test
    public void saveAndGetTest() {
        String name = "Jack London";
        Author expected = new Author(name);
        authorDao.save(expected);

        try {
            Console.main();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        Collection<Author> actual = authorDao.getByName(name);
        assertTrue(actual.size() == 1);
        assertEquals(actual.toArray()[0], expected);

    }
}
