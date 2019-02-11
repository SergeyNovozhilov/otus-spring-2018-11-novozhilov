package ru.otus;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

import java.sql.SQLException;

@SpringBootApplication
public class App {

	public static void main(String[] args) throws SQLException {
		ApplicationContext context = SpringApplication.run(App.class, args);
//		GenreManager genreManager = context.getBean(GenreManager.class);
//		Genre g = genreManager.create("Genre");
//
//		BookManager bookManager = context.getBean(BookManager.class);
//
//		AuthorManager authorManager = context.getBean(AuthorManager.class);
//
//		Book bk = bookManager.create("Book");
//		bookManager.addGenre(bk, "Genre");
//		bookManager.addAuthor(bk, "author");
//		try {
//			bookManager.update(bk);
//		} catch (DataBaseException e) {
//			e.printStackTrace();
//		}
//
//		Book bk1 = bookManager.create("Book 1");
//		bookManager.addGenre(bk1, "Genre 1");
//		bookManager.addAuthor(bk1, "author");
//		try {
//			bookManager.update(bk1);
//		} catch (DataBaseException e) {
//			e.printStackTrace();
//		}
//
//		Collection<Book> bks = null;
//		try {
//			bks = bookManager.get("", "", "");
//		} catch (NotFoundException e) {
//			e.printStackTrace();
//		}
//
//		if (bks != null) {
//			System.out.println("--- Books ----");
//			bks.forEach(b -> {
//				System.out.println(b.getTitle());
//				System.out.println(b.getGenre().getName());
//
//				if (b.getComments() != null) {
//					b.getComments().forEach(c -> System.out.println(c.getComment()));
//				}
//
//				if (b.getAuthors() != null) {
//					b.getAuthors().forEach(c -> System.out.println(c.getName()));
//				}
//			});
//		}
//
//		Collection<Author> as = null;
//		try {
//			as = authorManager.get("", "", "");
//		} catch (NotFoundException e) {
//			e.printStackTrace();
//		}
//		if (as != null && !as.isEmpty()) {
//            System.out.println("---- Authors ----");
//		    as.forEach(au -> {
//                System.out.println(au.getName());
//                Collection<Book> books = au.getBooks();
//                    if (books != null && !books.isEmpty()) {
//                        books.forEach(b -> {
//                            System.out.println("  " + b.getTitle());
//                        });
//                    }
//                    Collection<Genre> gs = au.getGenres();
//				if (gs != null && !gs.isEmpty()) {
//					gs.forEach(b -> {
//						System.out.println("  " + b.getName());
//					});
//				}
//            });
//        }
//
//		try {
//			bookManager.delete(bk);
//		} catch (DataBaseException e) {
//			e.printStackTrace();
//		}
//
//		try {
//			bookManager.delete(bk1);
//		} catch (DataBaseException e) {
//			e.printStackTrace();
//		}
//
//		Console.main(args);
	}
}
