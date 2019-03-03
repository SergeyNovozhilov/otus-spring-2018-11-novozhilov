package ru.otus.Dtos;

import lombok.Data;

import javax.persistence.*;
import java.util.Objects;
import java.util.UUID;

@Data
@Table(name = "GENRES")
@Entity
public class GenreDto extends Base {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;
    private String name;

    public GenreDto() {
    }

    public GenreDto(String name) {
        this.name = name;
    }

    public GenreDto(UUID id, String name) {
        this.id = id;
        this.name = name;
    }

    @Override
    public void print() {
        System.out.println("Name: " + this.name);
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
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
        GenreDto genreDto = (GenreDto) o;
        return Objects.equals(id, genreDto.id) && Objects.equals(name, genreDto.name);
    }

    @Override public int hashCode() {

        return Objects.hash(id, name);
    }
}
