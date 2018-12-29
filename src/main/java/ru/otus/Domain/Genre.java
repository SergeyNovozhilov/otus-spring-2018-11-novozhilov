package ru.otus.Domain;

import lombok.Setter;

import java.util.UUID;

@Setter
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
}
