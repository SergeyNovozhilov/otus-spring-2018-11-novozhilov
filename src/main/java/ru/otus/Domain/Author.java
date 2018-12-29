package ru.otus.Domain;

import lombok.Getter;

import java.util.UUID;

@Getter
public class Author extends Base{
	private String name;

	public Author(String name) {
		super();
		this.name = name;
	}

	public Author(UUID id ,String name) {
		super(id);
		this.name = name;
	}
}
