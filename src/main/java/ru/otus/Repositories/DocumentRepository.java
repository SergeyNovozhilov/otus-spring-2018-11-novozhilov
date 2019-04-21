package ru.otus.repositories;

import org.springframework.data.mongodb.repository.MongoRepository;
import ru.otus.entities.Book;

public interface DocumentRepository extends MongoRepository<Book, String> {


}
