package ru.otus.entities;

import lombok.Data;

import java.util.UUID;

@Data
public class Comment {
    private String id;
    private String comment;
    private Book book;


    public Comment() {
    }

    public Comment(String comment) {
        this.comment = comment;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Book getBook() {
        return book;
    }

    public void setBook(Book book) {
        this.book = book;
    }
}
