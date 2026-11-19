package com.example.fpvfleet.lab3.animals;

import java.util.Objects;

public abstract class Animal {

    private final String name;

    protected Animal(String name) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Ім'я тварини є обов'язковим");
        }
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public abstract String makeSound();

    @Override
    public String toString() {
        return getClass().getSimpleName() + " " + name;
    }

    @Override
    public boolean equals(Object o) {
        return o != null && o.getClass() == getClass() && name.equals(((Animal) o).name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(getClass(), name);
    }
}
