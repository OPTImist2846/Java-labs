import java.util.*;

public class Main {

    public static void main(String[] args) {
        System.out.println("ЗАПУСК ЛАБОРАТОРНОЇ РОБОТИ №3\n");

        testTask1And2();
        testTask3And4();
    }

    private static void testTask1And2() {
        System.out.println(" ТЕСТ ЗАВДАННЯ 1 (Map)");
        StudentRegistry registry = new StudentRegistry();
        registry.addStudent(new Student(101, "Олександр"));
        registry.addStudent(new Student(102, "Марія"));
        registry.displayAllStudents();

        System.out.println("\nТЕСТ ЗАВДАННЯ 2 (Set та підрахунок)");
        List<String> names = Arrays.asList("Анна", "Петро", "Анна", "Іван");
        System.out.println("Оригінальний список: " + names);
        System.out.println("Унікальні (Set): " + UniqueStorage.getUniqueElements(names));
        System.out.println("Кількість (Map): " + UniqueStorage.countOccurrences(names));
    }

    private static void testTask3And4() {
        System.out.println("ТЕСТ ЗАВДАННЯ 3 (Box<T>)");
        Box<Integer> integerBox = new Box<>();
        integerBox.putItem(123);
        System.out.println("У коробці з числами лежить: " + integerBox.getItem());

        System.out.println("\nТЕСТ ЗАВДАННЯ 4 (findMax)");
        Integer[] intArray = {3, 9, 1, 14, 5};
        System.out.println("Максимум у Integer[]: " + GenericUtils.findMax(intArray));
    }


}