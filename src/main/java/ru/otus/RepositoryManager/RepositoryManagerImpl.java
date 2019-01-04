package ru.otus.RepositoryManager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.otus.Dao.AuthorDao;
import ru.otus.Dao.BaseDao;
import ru.otus.Dao.BookDao;
import ru.otus.Dao.GenreDao;

@Service
public class RepositoryManagerImpl implements RepositoryManager {

    private AuthorDao authorDao;
    private BookDao bookDao;
    private GenreDao genreDao;

    @Override
    public <T extends BaseDao> T getDao(Class clazz) {
        switch (clazz.getCanonicalName()) {
            case "Author":
                return (T) authorDao;
            case "Book":
                return (T) bookDao;
            case "Genre":
                return (T) genreDao;
                default:
                    return null;
        }
    }

    @Autowired
    public void setAuthorDao(AuthorDao authorDao) {
        this.authorDao = authorDao;
    }

    @Autowired
    public void setBookDao(BookDao bookDao) {
        this.bookDao = bookDao;
    }

    @Autowired
    public void setGenreDao(GenreDao genreDao) {
        this.genreDao = genreDao;
    }
}
