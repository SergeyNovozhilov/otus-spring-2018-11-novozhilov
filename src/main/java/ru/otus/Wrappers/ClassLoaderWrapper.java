package ru.otus.Wrappers;

import java.io.InputStream;

public class ClassLoaderWrapper {
    public InputStream getResourceAsStream(Class clazz, String resource) {
        return clazz.getClassLoader().getResourceAsStream(resource);
    }
}
