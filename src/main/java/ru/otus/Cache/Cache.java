package ru.otus.Cache;

import org.springframework.stereotype.Service;
import ru.otus.entities.IEntity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class Cache {
	private final Map<Class<? extends IEntity>, List<? extends IEntity>> cache;

	public Cache() {
		this.cache = new HashMap<>();
	}

	public void add(Class<? extends IEntity> clazz, List objects) {
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

	public void deleteAll(Class<? extends IEntity> clazz) {
		if (!cache.containsKey(clazz)) {
			return;
		}
		cache.replace(clazz, cache.get(clazz), new ArrayList<>());
	}

	public List<? extends IEntity> get(Class<? extends IEntity> clazz) {
		if (!cache.containsKey(clazz)) {
			return new ArrayList<>();
		}
		return cache.get(clazz);
	}

	public IEntity get(Class<? extends IEntity> clazz, int index) {
		if (!cache.containsKey(clazz) || cache.get(clazz).size() < index - 1) {
			return null;
		}
		return cache.get(clazz).get(index);
	}
}
