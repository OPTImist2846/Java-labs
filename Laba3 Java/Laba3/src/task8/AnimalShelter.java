package task8;

import java.util.ArrayList;
import java.util.List;

public class AnimalShelter {
    private List<Dog> dogList = new ArrayList<>();
    private List<Animal> otherAnimalsList = new ArrayList<>();

    public void addAnimals(List<? super Dog> list, Dog dog) {
        list.add(dog);
    }

    public void addOtherAnimal(Animal animal) {
        otherAnimalsList.add(animal);
    }

    public void printAnimalSounds(List<? extends Animal> animals) {
        for (Animal animal : animals) {
            animal.makeSound();
        }
    }

    public List<Dog> getDogList() { return dogList; }
    public List<Animal> getOtherAnimalsList() { return otherAnimalsList; }
}