package ru.otus.Domain;


public class Result {
	private String name;
	private int correct;
	private int total;

	public void setName(String name) {
		this.name = name;
	}

	public void setCorrect(int correct) {
		this.correct = correct;
	}

	public void setTotal(int total) {
		this.total = total;
	}

	public String getResult() {
		return this.name + ": " + this.correct + " correct answers from " + this.total;
	}
}
