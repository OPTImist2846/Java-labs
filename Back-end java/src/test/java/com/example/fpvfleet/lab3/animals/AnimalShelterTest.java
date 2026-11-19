package com.example.fpvfleet.lab3.animals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class AnimalShelterTest {

    private AnimalShelter shelter;
    private ByteArrayOutputStream buffer;
    private PrintStream out;

    @BeforeEach
    void setUp() {
        shelter = new AnimalShelter();
        buffer = new ByteArrayOutputStream();
        out = new PrintStream(buffer, true, StandardCharsets.UTF_8);
    }

    @Test
    void makeSound_eachAnimal_returnsOwnSound() {
        assertThat(new Dog("Рекс").makeSound()).isEqualTo("Гав!");
        assertThat(new Cat("Мурка").makeSound()).isEqualTo("Няв!");
        assertThat(new Labrador("Бадді").makeSound()).startsWith("Гав-гав!");
    }

    @Test
    void addAnimals_dogsAndLabradors_addedToDogList() {
        List<Dog> target = new ArrayList<>();

        AnimalShelter.addAnimals(target, List.of(new Dog("Рекс"), new Labrador("Бадді")));

        assertThat(target).extracting(Animal::getName).containsExactly("Рекс", "Бадді");
    }

    @Test
    void addAnimals_labradorList_acceptedThroughUpperBound() {
        List<Labrador> labradors = List.of(new Labrador("Бадді"), new Labrador("Лакі"));
        List<Dog> target = new ArrayList<>();

        AnimalShelter.addAnimals(target, labradors);

        assertThat(target).hasSize(2).allMatch(dog -> dog instanceof Labrador);
    }

    @Test
    void addAnimals_animalOrObjectTarget_acceptedThroughLowerBound() {
        List<Animal> animals = new ArrayList<>(List.of(new Cat("Мурка")));
        List<Object> objects = new ArrayList<>();

        AnimalShelter.addAnimals(animals, List.of(new Dog("Рекс")));
        AnimalShelter.addAnimals(objects, List.of(new Labrador("Бадді")));

        assertThat(animals).hasSize(2);
        assertThat(objects).containsExactly(new Labrador("Бадді"));
    }

    @Test
    void addAnimals_listWithNull_throwsAndAddsNothing() {
        List<Dog> target = new ArrayList<>();

        assertThatThrownBy(() -> AnimalShelter.addAnimals(target, Arrays.asList(new Dog("Рекс"), null)))
                .isInstanceOf(NullPointerException.class);

        assertThat(target).isEmpty();
    }

    @Test
    void addDogs_storesDogsInDogList() {
        shelter.addDogs(List.of(new Dog("Рекс"), new Labrador("Бадді")));

        assertThat(shelter.getDogs()).hasSize(2);
        assertThat(shelter.getOtherAnimals()).isEmpty();
    }

    @Test
    void addOtherAnimals_cat_storesInOtherList() {
        shelter.addOtherAnimals(List.of(new Cat("Мурка")));

        assertThat(shelter.getOtherAnimals()).containsExactly(new Cat("Мурка"));
        assertThat(shelter.size()).isEqualTo(1);
    }

    @Test
    void addOtherAnimals_containsDog_throwsAndAddsNothing() {
        List<Animal> mixed = List.of(new Cat("Мурка"), new Labrador("Бадді"));

        assertThatThrownBy(() -> shelter.addOtherAnimals(mixed))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("собак");

        assertThat(shelter.size()).isZero();
    }

    @Test
    void printAnimalSounds_shelterWithAnimals_printsDogsThenOthers() {
        shelter.addDogs(List.of(new Dog("Рекс"), new Labrador("Бадді")));
        shelter.addOtherAnimals(List.of(new Cat("Мурка")));

        shelter.printAnimalSounds(out);

        assertThat(buffer.toString(StandardCharsets.UTF_8).lines().toList()).containsExactly(
                "Dog Рекс: Гав!",
                "Labrador Бадді: Гав-гав! (радісно виляє хвостом)",
                "Cat Мурка: Няв!");
    }

    @Test
    void printAnimalSounds_emptyShelter_printsEmptyMessage() {
        shelter.printAnimalSounds(out);

        assertThat(buffer.toString(StandardCharsets.UTF_8).trim()).isEqualTo("Притулок порожній");
    }

    @Test
    void getDogs_returnsUnmodifiableCopy() {
        shelter.addDogs(List.of(new Dog("Рекс")));

        List<Dog> dogs = shelter.getDogs();

        assertThatThrownBy(() -> dogs.add(new Dog("Сірко"))).isInstanceOf(UnsupportedOperationException.class);
    }

    @Test
    void newAnimal_blankName_throwsIllegalArgument() {
        assertThatThrownBy(() -> new Cat(" ")).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void equals_sameNameDifferentClass_notEqual() {
        assertThat(new Dog("Бадді")).isNotEqualTo(new Labrador("Бадді"))
                .isEqualTo(new Dog("Бадді"))
                .hasSameHashCodeAs(new Dog("Бадді"));
    }
}
