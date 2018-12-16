package ru.otus.Wrappers;

import org.springframework.stereotype.Service;

import java.io.InputStream;

@Service
public class ClassLoaderWrapper {
    public InputStream getResourceAsStream(Class clazz, String resource) {
        return clazz.getClassLoader().getResourceAsStream(resource);
    }
}
