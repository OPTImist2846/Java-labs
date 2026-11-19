package com.example.fpvfleet.lab3.animals;

public class Labrador extends Dog {

    public Labrador(String name) {
        super(name);
    }

    @Override
    public String makeSound() {
        return "Гав-гав! (радісно виляє хвостом)";
    }
}
