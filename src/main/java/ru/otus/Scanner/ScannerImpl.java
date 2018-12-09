package ru.otus.Scanner;

import org.springframework.stereotype.Service;

import java.util.Scanner;

@Service
public class ScannerImpl implements IScanner{

	private Scanner scanner;

	public ScannerImpl() {
		scanner = new Scanner(System.in);
	}

	@Override public String getInput() {
		return scanner.nextLine();
	}
}
