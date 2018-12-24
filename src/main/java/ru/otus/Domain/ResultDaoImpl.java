package ru.otus.Domain;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ResultDaoImpl implements ResultDao {

	private Map<String, List<Result>> storage = new HashMap<>();

	@Override
	public void save(Result result) {
		List<Result> results = null;

		if (storage == null) {
			storage = new HashMap<>();
		}
		results = storage.get(result.getName());
		if (results == null) {
			results = new ArrayList<>();
		}
		results.add(result);
		storage.put(result.getName(), results);
	}

	@Override
	public List<Result> get(String name) {
		return storage.getOrDefault(name, new ArrayList<>());
	}

	@Override
	public List<String> getAllNames() {
		return new ArrayList<>(storage.keySet());
	}
}
