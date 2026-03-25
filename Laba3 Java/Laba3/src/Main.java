import java.util.HashMap;
import java.util.Map;

class Student {
    private int studentId;
    private String name;

    public Student(int studentId, String name) {
        this.studentId = studentId;
        this.name = name;
    }

    public int getStudentId() { return studentId; }

    @Override
    public String toString() {
        return "Студент {ID=" + studentId + ", Ім'я='" + name + "'}";
    }
}

class StudentRegistry {
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

}