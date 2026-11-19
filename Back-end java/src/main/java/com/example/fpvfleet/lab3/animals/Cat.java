package com.example.fpvfleet.lab3.animals;

public class Cat extends Animal {

    public Cat(String name) {
        super(name);
    }

    @Override
    public String makeSound() {
        return "Няв!";
    }
}
