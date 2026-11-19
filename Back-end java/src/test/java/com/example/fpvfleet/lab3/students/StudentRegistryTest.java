package com.example.fpvfleet.lab3.students;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class StudentRegistryTest {

    private static final Student IVAN = new Student("KB-001", "Іван", "Петренко", "КН-31", 91.5);
    private static final Student OLHA = new Student("KB-002", "Ольга", "Коваль", "КН-32", 88.0);
    private static final Student PETRO = new Student("KB-003", "Петро", "Петренко", "КН-31", 74.0);

    private StudentRegistry registry;

    @BeforeEach
    void setUp() {
        registry = new StudentRegistry();
    }

    @Test
    void addStudent_newId_storesStudentAndReturnsTrue() {
        boolean added = registry.addStudent(IVAN);

        assertThat(added).isTrue();
        assertThat(registry.size()).isEqualTo(1);
        assertThat(registry.findById("KB-001")).contains(IVAN);
    }

    @Test
    void addStudent_duplicateId_keepsOriginalAndReturnsFalse() {
        registry.addStudent(IVAN);
        Student impostor = new Student("KB-001", "Інший", "Студент", "КН-99", 50.0);

        boolean added = registry.addStudent(impostor);

        assertThat(added).isFalse();
        assertThat(registry.findById("KB-001")).contains(IVAN);
    }

    @Test
    void addStudent_null_throwsNullPointerException() {
        assertThatThrownBy(() -> registry.addStudent(null)).isInstanceOf(NullPointerException.class);
    }

    @Test
    void removeStudent_existingId_returnsRemovedStudent() {
        registry.addStudent(IVAN);
        registry.addStudent(OLHA);

        Optional<Student> removed = registry.removeStudent("KB-001");

        assertThat(removed).contains(IVAN);
        assertThat(registry.getAllStudents()).containsExactly(OLHA);
    }

    @Test
    void removeStudent_unknownId_returnsEmptyAndKeepsRegistry() {
        registry.addStudent(IVAN);

        Optional<Student> removed = registry.removeStudent("KB-404");

        assertThat(removed).isEmpty();
        assertThat(registry.size()).isEqualTo(1);
    }

    @Test
    void findById_unknownId_returnsEmpty() {
        assertThat(registry.findById("KB-404")).isEmpty();
    }

    @Test
    void findByLastName_ignoresCase_returnsAllNamesakes() {
        registry.addStudent(IVAN);
        registry.addStudent(OLHA);
        registry.addStudent(PETRO);

        List<Student> result = registry.findByLastName("петренко");

        assertThat(result).containsExactly(IVAN, PETRO);
    }

    @Test
    void findByGroup_unknownGroup_returnsEmptyList() {
        registry.addStudent(IVAN);

        List<Student> result = registry.findByGroup("ФІ-11");

        assertThat(result).isEmpty();
    }

    @Test
    void findByGroup_existingGroup_returnsGroupMembers() {
        registry.addStudent(IVAN);
        registry.addStudent(OLHA);
        registry.addStudent(PETRO);

        List<Student> result = registry.findByGroup("КН-31");

        assertThat(result).containsExactly(IVAN, PETRO);
    }

    @Test
    void getAllStudents_keepsInsertionOrderAndIsUnmodifiable() {
        registry.addStudent(OLHA);
        registry.addStudent(IVAN);

        List<Student> all = registry.getAllStudents();

        assertThat(all).containsExactly(OLHA, IVAN);
        assertThatThrownBy(() -> all.add(PETRO)).isInstanceOf(UnsupportedOperationException.class);
    }

    @Test
    void printAllStudents_withStudents_printsOneLinePerStudent() {
        registry.addStudent(IVAN);
        registry.addStudent(OLHA);
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();

        registry.printAllStudents(new PrintStream(buffer, true, StandardCharsets.UTF_8));

        assertThat(buffer.toString(StandardCharsets.UTF_8).lines().toList())
                .containsExactly("KB-001 | Петренко Іван | КН-31 | 91.5", "KB-002 | Коваль Ольга | КН-32 | 88.0");
    }

    @Test
    void printAllStudents_emptyRegistry_printsEmptyMessage() {
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();

        registry.printAllStudents(new PrintStream(buffer, true, StandardCharsets.UTF_8));

        assertThat(buffer.toString(StandardCharsets.UTF_8).trim()).isEqualTo("Реєстр порожній");
    }

    @Test
    void newStudent_blankId_throwsIllegalArgument() {
        assertThatThrownBy(() -> new Student(" ", "Іван", "Петренко", "КН-31", 90))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void newStudent_gradeOutOfRange_throwsIllegalArgument() {
        assertThatThrownBy(() -> new Student("KB-9", "Іван", "Петренко", "КН-31", 100.5))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
