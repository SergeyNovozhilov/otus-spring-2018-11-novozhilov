package ru.otus.Domain;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import java.util.UUID;

@Table(name = "COMMENTS")
@Entity
public class Comment extends Base{
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private UUID id;
	private String comment;
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "book_id")
	@OnDelete(action = OnDeleteAction.CASCADE)
	private Book book;

	public UUID getId() {
		return id;
	}

	public Comment() {
	}

	public Comment(String name) {
		this.comment = name;
	}

	@Override
	public void print() {
		System.out.println(this.comment);
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}
}
