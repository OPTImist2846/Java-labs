package com.example.fpvfleet.lab3.students;

import java.util.Locale;
import java.util.Objects;

public record Student(String id, String firstName, String lastName, String group, double averageGrade) {

    public Student {
        if (id == null || id.isBlank()) {
            throw new IllegalArgumentException("Номер залікової книжки є обов'язковим");
        }
        Objects.requireNonNull(firstName, "firstName");
        Objects.requireNonNull(lastName, "lastName");
        Objects.requireNonNull(group, "group");
        if (averageGrade < 0 || averageGrade > 100) {
            throw new IllegalArgumentException("Середній бал має бути в межах 0..100");
        }
    }

    public String fullName() {
        return lastName + " " + firstName;
    }

    @Override
    public String toString() {
        return String.format(Locale.ROOT, "%s | %s | %s | %.1f", id, fullName(), group, averageGrade);
    }
}
