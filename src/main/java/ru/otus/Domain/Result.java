package ru.otus.Domain;


public class Result {
	private String name;
	private int correct;
	private int total;
	private String resultString;

	public void setName(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
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

	public int getCorrect() {
		return correct;
	}

	public int getTotal() {
		return total;
	}

	public String getResultString() {
		return resultString;
	}
}
