package ru.otus.Domain;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.UUID;

//@Table(name = "GENRES")
@Entity
@Data
public class Genre extends Base{
	private String name;

	@Override public UUID getId() {
		return id;
	}

	@Id
	private UUID id;

	public Genre(UUID id) {
		this.id = id;
	}

	public Genre() {
		this.id = UUID.randomUUID();
	}

	public Genre(String name) {
		this();
		this.name = name;
	}

	public Genre(UUID id, String name) {
		this(id);
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
