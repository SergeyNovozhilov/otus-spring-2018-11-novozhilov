package ru.otus.Domain;

import lombok.Data;

import javax.persistence.*;
import java.util.Objects;
import java.util.UUID;

@Data
@Table(name = "GENRES")
@Entity
public class Genre extends Base{
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private UUID id;
	private String name;

	public Genre() {
	}

	public Genre(String name) {
		this.name = name;
	}

	public Genre(UUID id, String name) {
		this.id = id;
		this.name = name;
	}

	@Override
	public void print() {
		System.out.println("Name: " + this.name);
	}

	public UUID getId() {
		return id;
	}

	public String getName() {
		return name;
	}
}
