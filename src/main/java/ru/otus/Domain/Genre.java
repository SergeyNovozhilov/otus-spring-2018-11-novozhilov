package ru.otus.Domain;

import lombok.Data;

import java.util.UUID;

@Data
public class Genre extends Base{
	private String name;

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
