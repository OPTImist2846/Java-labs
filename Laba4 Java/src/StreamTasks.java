import java.util.*;
import java.util.stream.Collectors;

public class StreamTasks {

    public static Optional<String> task1(List<String> list) {
        return Optional.of(list.stream()
                .filter(s -> s.startsWith("X") && s.length() > 5)
                .findFirst()
                .orElse("Default"));
    }

    public static List<Integer> task2(List<Optional<Integer>> optionals) {
        return optionals.stream()
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toList());
    }

    public static Optional<String> task3(List<String> names) {
        return names.stream()
                .max(Comparator.comparingInt(String::length));
    }

    public static Map<String, Employee> task4(List<Employee> employees) {
        return employees.stream().collect(Collectors.groupingBy(
                e -> {
                    if (e.getSalary() < 3000) return "< 3000";
                    else if (e.getSalary() <= 5000) return "3000-5000";
                    else return "> 5000";
                },
                Collectors.collectingAndThen(
                        Collectors.maxBy(Comparator.comparingDouble(Employee::getSalary)),
                        Optional::get // Дістаємо Employee з Optional
                )
        ));
    }
}