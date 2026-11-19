package com.example.fpvfleet.lab3.animals;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

public class AnimalShelter {

    private final List<Dog> dogs = new ArrayList<>();
    private final List<Animal> otherAnimals = new ArrayList<>();

    public static void addAnimals(List<? super Dog> target, Collection<? extends Dog> newDogs) {
        Objects.requireNonNull(target, "target");
        Objects.requireNonNull(newDogs, "newDogs");
        for (Dog dog : newDogs) {
            Objects.requireNonNull(dog, "Не можна додати null");
        }
        target.addAll(newDogs);
    }

    public static void printAnimalSounds(List<? extends Animal> animals, PrintStream out) {
        for (Animal animal : animals) {
            out.println(animal + ": " + animal.makeSound());
        }
    }

    public void addDogs(Collection<? extends Dog> newDogs) {
        addAnimals(dogs, newDogs);
    }

    public void addOtherAnimals(Collection<? extends Animal> animals) {
        Objects.requireNonNull(animals, "animals");
        for (Animal animal : animals) {
            Objects.requireNonNull(animal, "Не можна додати null");
            if (animal instanceof Dog) {
                throw new IllegalArgumentException(animal + " має потрапити до списку собак");
            }
        }
        otherAnimals.addAll(animals);
    }

    public void printAnimalSounds(PrintStream out) {
        if (dogs.isEmpty() && otherAnimals.isEmpty()) {
            out.println("Притулок порожній");
            return;
        }
        printAnimalSounds(dogs, out);
        printAnimalSounds(otherAnimals, out);
    }

    public List<Dog> getDogs() {
        return List.copyOf(dogs);
    }

    public List<Animal> getOtherAnimals() {
        return List.copyOf(otherAnimals);
    }

    public int size() {
        return dogs.size() + otherAnimals.size();
    }
}
