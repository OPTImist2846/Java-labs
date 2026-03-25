package task1;

import java.util.HashMap;
import java.util.Map;

public class  StudentRegistry {
    private Map<Integer, Student> registry = new HashMap<>();

    public void addStudent(Student student) {
        registry.put(student.getStudentId(), student);
        System.out.println("Додано: " + student);
    }

    public void removeStudent(int studentId) {
        if (registry.containsKey(studentId)) {
            registry.remove(studentId);
            System.out.println("Студента з ID " + studentId + " видалено.");
        } else {
            System.out.println("Помилка: Студента з ID " + studentId + " не знайдено.");
        }
    }

    public void findStudent(int studentId) {
        Student found = registry.get(studentId);
        if (found != null) {
            System.out.println("Знайдено: " + found);
        } else {
            System.out.println("Студента з ID " + studentId + " не існує.");
        }
    }

    public void displayAllStudents() {
        System.out.println("--- Список усіх студентів ---");
        for (Student s : registry.values()) {
            System.out.println(s);
        }
    }
}
