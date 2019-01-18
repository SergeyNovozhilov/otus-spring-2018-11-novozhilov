package ru.otus.Domain;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.UUID;

@Table(name = "GENRES")
@Entity
@Data
public class Genre extends Base{
	private String name;

	public Genre() {
		super();
	}

	public Genre(String name) {
		super();
		this.name = name;
	}

	public Genre(UUID id, String name) {
		super(id);
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
