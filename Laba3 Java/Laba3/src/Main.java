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
        System.out.println("=== ТЕСТ ЗАВДАННЯ 3 (Box<T>) ===");

        Box<Integer> integerBox = new Box<>();
        integerBox.putItem(42);
        System.out.println("У коробці з числами лежить: " + integerBox.getItem());

        Box<String> stringBox = new Box<>();
        stringBox.putItem("Секретний лист");
        System.out.println("У коробці з текстом лежить: " + stringBox.getItem());

        Box<Double> doubleBox = new Box<>();
        doubleBox.putItem(3.14159);
        System.out.println("У коробці з дробовими числами лежить: " + doubleBox.getItem());

        Box<Student> studentBox = new Box<>();
        studentBox.putItem(new Student(999, "Олесь"));
        System.out.println("У коробці зі студентом лежить: " + studentBox.getItem());

        System.out.println("\nТЕСТ ЗАВДАННЯ 4 (findMax)");
        Integer[] intArray = {3, 9, 1, 14, 5};
        System.out.println("Максимум у Integer[]: " + GenericUtils.findMax(intArray));
    }

    private static void testTask5And6() {
        System.out.println("ТЕСТ ЗАВДАННЯ 5 (Клас Pair)");

        Pair<String, Integer> pair1 = new Pair<>("Яблуко", 10);
        Pair<String, Integer> pair2 = new Pair<>("Яблуко", 10);
        Pair<String, Integer> pair3 = new Pair<>("Банан", 5);

        Pair<Integer, String> pair4 = new Pair<>(10, "Яблуко");

        System.out.println("Пара 1: " + pair1);
        System.out.println("Пара 4: " + pair4);
        System.out.println("Пара 1 та Пара 2 однакові? -> " + pair1.comparePairs(pair2)); // true
        System.out.println("Пара 1 та Пара 3 однакові? -> " + pair1.comparePairs(pair3)); // false

        System.out.println("\nТЕСТ ЗАВДАННЯ 6 (? extends Shape)");

        List<Circle> circles = Arrays.asList(new Circle(2.0), new Circle(3.0));

        List<Rectangle> rectangles = Arrays.asList(new Rectangle(2, 5), new Rectangle(3, 4));

        System.out.println("Загальна площа кіл: " + ShapeCalculator.calculateTotalArea(circles));
        System.out.println("Загальна площа прямокутників: " + ShapeCalculator.calculateTotalArea(rectangles));
    }

    private static void testTask7() {
        System.out.println("=== ТЕСТ ЗАВДАННЯ 7 (? super Integer) ===");

        List<Integer> intList = new ArrayList<>();
        List<Number> numList = new ArrayList<>();

        System.out.print("Додаємо в List<Integer>: ");
        NumberAdder.addNumbers(intList);

        System.out.print("Додаємо в List<Number>: ");
        NumberAdder.addNumbers(numList);
    }
}