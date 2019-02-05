package ru.otus.Domain;

import lombok.Data;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import java.util.UUID;

@Data
@Table(name = "COMMENTS")
@Entity
public class Comment extends Base{
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;
    private String comment;
    @ManyToOne
    @JoinColumn(name = "book_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Book book;

    public Comment() {
    }

    public Comment(String comment) {
        this.comment = comment;
    }

    @Override
    public void print() {
        System.out.println(this.comment);
    }
}
