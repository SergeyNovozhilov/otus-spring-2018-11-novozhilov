package ru.otus.Domain;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class Result {
	private String name;
	private String score;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getScore() {
		return score;
	}

	public void setScore(String score) {
		this.score = score;
	}
	public String getResult() {
		return this.name + ": " + this.score;
	}
}
