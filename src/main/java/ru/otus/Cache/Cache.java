package ru.otus.Cache;

import org.springframework.stereotype.Service;
import ru.otus.Domain.Base;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class Cache {
	private Map<Class<? extends Base>, List<? extends Base>> cache;

	public Cache() {
		this.cache = new HashMap<>();
	}

	public void add(Class<? extends Base> clazz, List objects) {
		this.deleteAll(clazz);
		this.cache.get(clazz).addAll(objects);
	}

	public void delete(Class<?> clazz, int index) {
		if (!this.cache.containsKey(clazz)) {
			return;
		}
		this.cache.get(clazz).remove(index);
	}

	public void deleteAll(Class<? extends Base> clazz) {
		if (!this.cache.containsKey(clazz)) {
			return;
		}
		this.cache.replace(clazz, this.cache.get(clazz), new ArrayList<>());
	}

	public List<? extends Base> get(Class<? extends Base> clazz) {
		if (!this.cache.containsKey(clazz)) {
			return new ArrayList<>();
		}
		return this.cache.get(clazz);
	}

	public Base get(Class<? extends Base> clazz, int index) {
		if (!this.cache.containsKey(clazz) || this.cache.get(clazz).size() < index - 1) {
			return null;
		}
		return this.cache.get(clazz).get(index);
	}
}
