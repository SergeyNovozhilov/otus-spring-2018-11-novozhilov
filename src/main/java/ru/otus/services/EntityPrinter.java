package ru.otus.services;

import ru.otus.entities.Author;
import ru.otus.entities.Book;
import ru.otus.entities.Entity;
import ru.otus.entities.Genre;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class EntityPrinter {
   public static void print(Entity entity) {
      print(Collections.singletonList(entity));
   }

   public static void print(Collection<Entity> entities) {
      if (entities != null && !entities.isEmpty()) {
         if (entities.iterator().next() instanceof Genre) {
            Collection<Genre> genres = entities.stream().map(e -> (Genre)e).collect(Collectors.toSet());
            printGenres(genres);
            return;
         }

         if (entities.iterator().next() instanceof Author) {
            Collection<Author> authors = entities.stream().map(e -> (Author)e).collect(Collectors.toSet());
            printAuthors(authors);
            return;
         }

         if (entities.iterator().next() instanceof Book) {
            Collection<Book> books = entities.stream().map(e -> (Book)e).collect(Collectors.toSet());
            printBooks(books);
            return;
         }
      }
   }

   private static void printGenres(Collection<Genre> genres) {
      List<Genre> array = new ArrayList<>(genres);
      for (int i = 0; i < array.size(); i ++) {
         Genre genre = array.get(i);
         System.out.println(i + ")");
         System.out.println(genre.getName());
      }
   }

   private static void printAuthors(Collection<Author> authors) {
      List<Author> array = new ArrayList<>(authors);
      for (int i = 0; i < array.size(); i ++) {
         Author author = array.get(i);
         System.out.println(i + ")");
         System.out.println(author.getName());
         if (author.getBooks() != null) {
            System.out.println("Books");
            author.getBooks().forEach(book -> {
               System.out.println(" " + book.getTitle());
               System.out.println(" Genre: " + book.getGenre().getName());
            });
         }
      }
   }

   private static void printBooks(Collection<Book> books) {
      List<Book> array = new ArrayList<>(books);
      for (int i = 0; i < array.size(); i ++) {
         Book book = array.get(i);
         System.out.println(i + ")");
         System.out.println("Title: " + book.getTitle());
         if (book.getGenre() != null) {
            System.out.println("  Genre: " + book.getGenre().getName());
         }
         if (book.getAuthors() != null) {
            System.out.println("  Authors: ");
            book.getAuthors().forEach(a -> System.out.println("   " + a.getName()));
         }
         if (book.getComments() != null) {
            System.out.println("  Comments: ");
            book.getComments().forEach(c -> System.out.println("   " + c.getComment()));
         }
      }
   }
}
