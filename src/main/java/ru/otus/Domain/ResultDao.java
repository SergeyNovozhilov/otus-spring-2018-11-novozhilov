package ru.otus.Domain;

import java.util.List;

public interface ResultDao {
	void save(Result result);
	List<Result> get(String name);
	List<String> getAllNames();
}
