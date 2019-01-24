package ru.otus.Domain;

import javax.persistence.*;
import java.util.UUID;

@Table(name = "GENRES")
@Entity
public class Genre extends Base{
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private UUID id;
	private String name;

	public UUID getId() {
		return id;
	}

	public Genre() {
	}

	public Genre(UUID id, String name) {
		this.id = id;
		this.name = name;
	}

	public Genre(String name) {
		this.name = name;
	}

	@Override
	public void print() {
		System.out.println("Name: " + this.name);
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}
