package com.example.fpvfleet.lab3;

import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;

import static org.assertj.core.api.Assertions.assertThat;

class Lab3DemoTest {

    @Test
    void run_printsResultOfEveryTask() {
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();

        Lab3Demo.run(new PrintStream(buffer, true, StandardCharsets.UTF_8));

        String output = buffer.toString(StandardCharsets.UTF_8);
        assertThat(output)
                .contains("=== 1. ", "=== 2. ", "=== 3. ", "=== 4. ", "=== 5. ", "=== 6. ", "=== 7. ", "=== 8. ")
                .contains("Повторне додавання KB-001: false")
                .contains("Унікальні: [Nazgul, Cinewhoop, Mobula]")
                .contains("Кількість: {Nazgul=3, Cinewhoop=2, Mobula=1}")
                .contains("Box<Integer>: 42")
                .contains("Integer: 42", "Double: 2.75", "Character: y", "String: Nazgul")
                .contains("(1, Nazgul) == (1, Nazgul): true")
                .contains("Змішано [Circle(r=1.0), Rectangle(2.0x3.0)]: 9.14")
                .contains("List<Integer>: [1, 2, 3, 4, 5, 6, 7, 8, 9, 10]")
                .contains("List<Object>: [початок, 1, 2, 3]")
                .contains("Labrador Бадді: Гав-гав!", "Cat Мурка: Няв!");
    }
}
