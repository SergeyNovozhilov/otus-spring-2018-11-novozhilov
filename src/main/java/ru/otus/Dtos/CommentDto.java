package ru.otus.Dtos;

import lombok.Data;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import java.util.UUID;

@Data
public class CommentDto extends Base {
    private UUID id;
    private String comment;
    private BookDto bookDto;

    public CommentDto() {
    }

    public CommentDto(String comment) {
        this.comment = comment;
    }

    @Override
    public void print() {
        System.out.println(this.comment);
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public BookDto getBookDto() {
        return bookDto;
    }

    public void setBookDto(BookDto bookDto) {
        this.bookDto = bookDto;
    }
}
