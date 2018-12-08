package ru.otus.Scanner;

import java.util.Scanner;

public class ScannerImpl implements IScanner{

	private Scanner scanner;

	public ScannerImpl() {
		scanner = new Scanner(System.in);
	}

	@Override public String getInput() {
		return scanner.nextLine();
	}
}
