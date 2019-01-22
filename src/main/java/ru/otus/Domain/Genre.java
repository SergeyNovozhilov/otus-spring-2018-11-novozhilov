package ru.otus.Domain;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Objects;
import java.util.UUID;

@Data
@EqualsAndHashCode
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

	@Override public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;
		Genre genre = (Genre) o;
		return Objects.equals(name, genre.name);
	}

	@Override public int hashCode() {

		return Objects.hash(name);
	}
}
