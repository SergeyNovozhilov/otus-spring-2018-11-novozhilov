package ru.otus.Domain;


public class Result {
	private String name;
	private int correct;
	private int total;
	private String resultString;

	public void setName(String name) {
		this.name = name;
	}

	public void setCorrect(int correct) {
		this.correct = correct;
	}

	public void setTotal(int total) {
		this.total = total;
	}

	public void setResultString(String resultString) {
		this.resultString = resultString;
	}

	public String getResult() {
		return this.name + ": " + this.correct + " " + resultString + " " + this.total;
	}
}
