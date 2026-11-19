package com.example.fpvfleet.lab3.students;

import java.io.PrintStream;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Predicate;

public class StudentRegistry {

    private final Map<String, Student> students = new LinkedHashMap<>();

    public boolean addStudent(Student student) {
        Objects.requireNonNull(student, "student");
        return students.putIfAbsent(student.id(), student) == null;
    }

    public Optional<Student> removeStudent(String id) {
        return Optional.ofNullable(students.remove(id));
    }

    public Optional<Student> findById(String id) {
        return Optional.ofNullable(students.get(id));
    }

    public List<Student> findByLastName(String lastName) {
        return filter(students.values(), student -> student.lastName().equalsIgnoreCase(lastName));
    }

    public List<Student> findByGroup(String group) {
        return filter(students.values(), student -> student.group().equalsIgnoreCase(group));
    }

    public List<Student> getAllStudents() {
        return List.copyOf(students.values());
    }

    public int size() {
        return students.size();
    }

    public void printAllStudents(PrintStream out) {
        if (students.isEmpty()) {
            out.println("Реєстр порожній");
            return;
        }
        students.values().forEach(out::println);
    }

    private static List<Student> filter(Collection<Student> source, Predicate<Student> condition) {
        return source.stream().filter(condition).toList();
    }
}
