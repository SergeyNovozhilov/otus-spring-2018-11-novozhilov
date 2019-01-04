package ru.otus.Domain;

import lombok.Getter;

import java.util.UUID;

@Getter
public abstract class Base {
	private UUID id;

	public Base(UUID id) {
		this.id = id;
	}

	public Base() {
		this.id = UUID.randomUUID();
	}

	public UUID getId () {
		return this.id;
	}

}
