package com.example.fpvfleet.lab3;

import com.example.fpvfleet.lab3.animals.AnimalShelter;
import com.example.fpvfleet.lab3.animals.Cat;
import com.example.fpvfleet.lab3.animals.Dog;
import com.example.fpvfleet.lab3.animals.Labrador;
import com.example.fpvfleet.lab3.generics.Box;
import com.example.fpvfleet.lab3.generics.GenericMethods;
import com.example.fpvfleet.lab3.generics.Pair;
import com.example.fpvfleet.lab3.shapes.Circle;
import com.example.fpvfleet.lab3.shapes.Rectangle;
import com.example.fpvfleet.lab3.shapes.Shape;
import com.example.fpvfleet.lab3.shapes.ShapeCalculator;
import com.example.fpvfleet.lab3.students.Student;
import com.example.fpvfleet.lab3.students.StudentRegistry;
import com.example.fpvfleet.lab3.unique.UniqueElements;
import com.example.fpvfleet.lab3.wildcards.IntegerLists;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public final class Lab3Demo {

    private Lab3Demo() {
    }

    public static void main(String[] args) {
        run(System.out);
    }

    public static void run(PrintStream out) {
        studentRegistry(out);
        uniqueElements(out);
        box(out);
        findMax(out);
        pair(out);
        upperBoundedWildcard(out);
        lowerBoundedWildcard(out);
        animalShelter(out);
    }

    private static void studentRegistry(PrintStream out) {
        header(out, 1, "Реєстр студентів (Map)");
        StudentRegistry registry = new StudentRegistry();
        registry.addStudent(new Student("KB-001", "Іван", "Петренко", "КН-31", 91.5));
        registry.addStudent(new Student("KB-002", "Ольга", "Коваль", "КН-32", 88.0));
        registry.addStudent(new Student("KB-003", "Петро", "Петренко", "КН-31", 74.0));
        out.println("Повторне додавання KB-001: " + registry.addStudent(new Student("KB-001", "X", "Y", "Z", 1)));
        registry.printAllStudents(out);
        out.println("Пошук KB-002: " + registry.findById("KB-002").map(Student::fullName).orElse("не знайдено"));
        out.println("Прізвище Петренко: " + registry.findByLastName("Петренко").size() + " студ.");
        out.println("Видалено: " + registry.removeStudent("KB-003").map(Student::fullName).orElse("нікого"));
        out.println("Залишилось: " + registry.size());
    }

    private static void uniqueElements(PrintStream out) {
        header(out, 2, "Унікальні елементи (Set) та підрахунок (Map)");
        List<String> drones = List.of("Nazgul", "Cinewhoop", "Nazgul", "Mobula", "Cinewhoop", "Nazgul");
        out.println("Вхід: " + drones);
        out.println("Унікальні: " + UniqueElements.unique(drones));
        out.println("Кількість: " + UniqueElements.countOccurrences(drones));
    }

    private static void box(PrintStream out) {
        header(out, 3, "Generic class Box<T>");
        Box<String> parts = new Box<>(2);
        parts.put("пропелери");
        parts.put("акумулятор");
        out.println("Box<String>: " + parts + ", заповнений: " + parts.isFull());
        out.println("Дістали: " + parts.take().orElseThrow());

        Box<Integer> numbers = new Box<>();
        numbers.put(42);
        out.println("Box<Integer>: " + numbers.take().orElseThrow());

        Box<Student> students = new Box<>();
        students.put(new Student("KB-010", "Анна", "Мельник", "КН-33", 95));
        out.println("Box<Student>: " + students.peek().map(Student::fullName).orElseThrow());

        Box<List<Double>> lists = new Box<>();
        lists.put(List.of(1.5, 2.5));
        out.println("Box<List<Double>>: " + lists.take().orElseThrow());
        out.println("Порожній ящик: " + lists.take().isPresent());
    }

    private static void findMax(PrintStream out) {
        header(out, 4, "Generic method findMax");
        out.println("Integer: " + GenericMethods.findMax(new Integer[]{3, -7, 42, 0}));
        out.println("Double: " + GenericMethods.findMax(new Double[]{-1.5, 2.75, 2.5}));
        out.println("Character: " + GenericMethods.findMax(new Character[]{'b', 'Z', 'y'}));
        out.println("String: " + GenericMethods.findMax(new String[]{"Mobula", "Nazgul", "Cinewhoop"}));
    }

    private static void pair(PrintStream out) {
        header(out, 5, "Generic Pair");
        Pair<Integer, String> first = Pair.of(1, "Nazgul");
        Pair<Integer, String> second = Pair.of(1, "Nazgul");
        Pair<String, List<Integer>> grades = Pair.of("Петренко", List.of(5, 4, 5));
        out.println("Pair<Integer, String>: " + first);
        out.println("Pair<String, List<Integer>>: " + grades);
        out.println(first + " == " + second + ": " + first.isSameAs(second));
        out.println(first + " == " + grades + ": " + first.isSameAs(grades));
        out.println("compare((1, a), (2, b)) = " + Pair.compare(Pair.of(1, "a"), Pair.of(2, "b")));
    }

    private static void upperBoundedWildcard(PrintStream out) {
        header(out, 6, "Upper-bounded wildcard: List<? extends Shape>");
        List<Circle> circles = List.of(new Circle(1), new Circle(2));
        List<Rectangle> rectangles = List.of(new Rectangle(2, 3), new Rectangle(4, 5));
        List<Shape> mixed = List.of(new Circle(1), new Rectangle(2, 3));
        out.println("Кола " + circles + ": " + format(ShapeCalculator.calculateTotalArea(circles)));
        out.println("Прямокутники " + rectangles + ": " + format(ShapeCalculator.calculateTotalArea(rectangles)));
        out.println("Змішано " + mixed + ": " + format(ShapeCalculator.calculateTotalArea(mixed)));
    }

    private static void lowerBoundedWildcard(PrintStream out) {
        header(out, 7, "Lower-bounded wildcard: List<? super Integer>");
        List<Integer> integers = new ArrayList<>();
        List<Number> numbers = new ArrayList<>(List.of(0.5));
        List<Object> objects = new ArrayList<>(List.of("початок"));
        IntegerLists.addToList(integers);
        IntegerLists.addToList(numbers);
        IntegerLists.addToList(objects, 1, 3);
        out.println("List<Integer>: " + integers);
        out.println("List<Number>: " + numbers);
        out.println("List<Object>: " + objects);
    }

    private static void animalShelter(PrintStream out) {
        header(out, 8, "Притулок для тварин");
        Dog rex = new Dog("Рекс");
        Labrador buddy = new Labrador("Бадді");
        Cat murka = new Cat("Мурка");
        AnimalShelter shelter = new AnimalShelter();
        shelter.addDogs(List.of(rex));
        shelter.addDogs(List.<Labrador>of(buddy));
        shelter.addOtherAnimals(List.of(murka));
        out.println("Собак: " + shelter.getDogs().size() + ", інших тварин: " + shelter.getOtherAnimals().size());
        shelter.printAnimalSounds(out);
    }

    private static void header(PrintStream out, int number, String title) {
        out.println();
        out.println("=== " + number + ". " + title + " ===");
    }

    private static String format(double value) {
        return String.format(Locale.ROOT, "%.2f", value);
    }
}
