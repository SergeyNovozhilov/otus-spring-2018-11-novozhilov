package ru.otus.cache;

import org.springframework.stereotype.Service;
import ru.otus.entities.Entity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class Cache {
	private final Map<Class<? extends Entity>, List<? extends Entity>> cache;

	public Cache() {
		this.cache = new HashMap<>();
	}

	public void add(Class<? extends Entity> clazz, List objects) {
		deleteAll(clazz);
		if (!cache.containsKey(clazz)) {
			cache.put(clazz, new ArrayList<>());
		}
		cache.get(clazz).addAll(objects);
	}

	public void delete(Class<?> clazz, int index) {
		if (!this.cache.containsKey(clazz)) {
			return;
		}
		cache.get(clazz).remove(index);
	}

	public void deleteAll(Class<? extends Entity> clazz) {
		if (!cache.containsKey(clazz)) {
			return;
		}
		cache.replace(clazz, cache.get(clazz), new ArrayList<>());
	}

	public List<? extends Entity> get(Class<? extends Entity> clazz) {
		if (!cache.containsKey(clazz)) {
			return new ArrayList<>();
		}
		return cache.get(clazz);
	}

	public Entity get(Class<? extends Entity> clazz, int index) {
		if (!cache.containsKey(clazz) || cache.get(clazz).size() < index - 1) {
			return null;
		}
		return cache.get(clazz).get(index);
	}
}
