package ru.otus.Scanner;

import org.springframework.stereotype.Service;

@Service
public class ScannerImpl implements Scanner {

	private java.util.Scanner scanner;

	public ScannerImpl() {
		scanner = new java.util.Scanner(System.in);
	}

	@Override public String getInput() {
		return scanner.nextLine();
	}
}
