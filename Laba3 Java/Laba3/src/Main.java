import java.util.*;

public class Main {
    public static void main(String[] args) {
        System.out.println("=== ТЕСТ ЗАВДАННЯ 1 (Map) ===");
        StudentRegistry registry = new StudentRegistry();

        registry.addStudent(new Student(101, "Олександр"));
        registry.addStudent(new Student(102, "Марія"));
        registry.addStudent(new Student(103, "Іван"));

        registry.displayAllStudents();
        registry.findStudent(102);
        registry.removeStudent(103);
        registry.displayAllStudents();

        System.out.println("\n=== ТЕСТ ЗАВДАННЯ 2 (Set та підрахунок) ===");

        List<String> names = Arrays.asList("Анна", "Петро", "Анна", "Іван", "Петро", "Анна");
        System.out.println("Оригінальний список: " + names);

        Set<String> uniqueNames = UniqueStorage.getUniqueElements(names);
        System.out.println("Унікальні елементи (Set): " + uniqueNames);

        Map<String, Integer> occurrences = UniqueStorage.countOccurrences(names);
        System.out.println("Кількість входжень (Map): " + occurrences);
    }
}