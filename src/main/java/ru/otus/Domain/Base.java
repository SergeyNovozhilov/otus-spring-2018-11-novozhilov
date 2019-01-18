package ru.otus.Domain;

import lombok.Getter;

import javax.persistence.Id;
import java.util.UUID;

@Getter
public abstract class Base {
	@Id
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

	public void print() {}

}
