import java.util.*;

public class Main {
    public static void main(String[] args) {
        System.out.println("=== ЛАБОРАТОРНА РОБОТА 4 (Stream API) ===\n");

        // 1
        List<String> strings = Arrays.asList("Apple", "Xenomorph", "Banana", "X-ray");
        System.out.println("Завдання 1: " + StreamTasks.task1(strings));

        // 2
        List<Optional<Integer>> optionals = Arrays.asList(Optional.of(10), Optional.empty(), Optional.of(20));
        System.out.println("Завдання 2: " + StreamTasks.task2(optionals));

        // 3
        List<String> names = Arrays.asList("Оля", "Олександр", "Іван");
        System.out.println("Завдання 3: " + StreamTasks.task3(names));

        //  4
        List<Employee> employees = Arrays.asList(
                new Employee("Анна", 2500), new Employee("Богдан", 2900),
                new Employee("Віктор", 4000), new Employee("Галина", 4800),
                new Employee("Дмитро", 6000), new Employee("Олена", 7500)
        );
        System.out.println("Завдання 4: " + StreamTasks.task4(employees));

        // 5
        List<Integer> numbers = Arrays.asList(1, 2, 3, 4, 5); // Непарні: 1, 3, 5. Добуток: 15
        System.out.println("Завдання 5: " + StreamTasks.task5(numbers));

        //  6
        Person p1 = new Person("Максим", Arrays.asList(new Person("Іван", null), new Person("Олег", null)));
        Person p2 = new Person("Юля", Arrays.asList(new Person("Олег", null), new Person("Яна", null)));
        System.out.println("Завдання 6: " + StreamTasks.task6(Arrays.asList(p1, p2)));

        // 7
        List<Transaction> transactions = Arrays.asList(
                new Transaction(100.50, "Продукти"), new Transaction(50.00, "Розваги"),
                new Transaction(200.00, "Продукти")
        );
        System.out.println("Завдання 7: " + StreamTasks.task7(transactions));

        // 8
        List<Product> products = Arrays.asList(
                new Product("Ноутбук", 1500), new Product("Телефон", 800), new Product("Планшет", 1000)
        );
        System.out.println("Завдання 8: " + StreamTasks.task8(products));

        // 9
        Map<Integer, Optional<String>> productMap = new HashMap<>();
        productMap.put(1, Optional.of("Мишка"));
        productMap.put(2, Optional.empty());
        productMap.put(3, Optional.of("Клавіатура"));
        System.out.println("Завдання 9: " + StreamTasks.task9(productMap));

        // 10
        Map<String, List<Double>> temps = new HashMap<>();
        temps.put("Київ", Arrays.asList(20.0, 22.0, 21.0)); // Середня ~21
        temps.put("Одеса", Arrays.asList(25.0, 26.0, 27.0)); // Середня ~26
        System.out.println("Завдання 10: " + StreamTasks.task10(temps));
    }
}