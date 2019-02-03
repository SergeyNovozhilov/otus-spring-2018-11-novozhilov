package ru.otus.Domain;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import java.util.UUID;

@Data
@EqualsAndHashCode
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

	@Override
	public void print() {
		System.out.println("Name: " + this.name);
	}
}
