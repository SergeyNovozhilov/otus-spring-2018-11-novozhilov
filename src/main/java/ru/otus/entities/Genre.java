package ru.otus.entities;

import lombok.Data;

import java.util.Collection;
import java.util.Objects;
import java.util.UUID;

@Data
public class Genre implements IEntity {
    private String id;
    private String name;
    private Collection<Book> book;


    public Genre() {
    }

    public Genre(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        Genre genre = (Genre) o;
        return Objects.equals(id, genre.id) && Objects.equals(name, genre.name);
    }

    @Override public int hashCode() {

        return Objects.hash(id, name);
    }
}
