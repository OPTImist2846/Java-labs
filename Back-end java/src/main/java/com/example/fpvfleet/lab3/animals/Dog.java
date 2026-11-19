package com.example.fpvfleet.lab3.animals;

public class Dog extends Animal {

    public Dog(String name) {
        super(name);
    }

    @Override
    public String makeSound() {
        return "Гав!";
    }
}
