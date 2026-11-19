package com.example.fpvfleet.lab3.generics;

import com.example.fpvfleet.lab3.students.Student;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class BoxTest {

    @Test
    void putAndTake_strings_returnsItemsInInsertionOrder() {
        Box<String> box = new Box<>();
        box.put("пропелери");
        box.put("акумулятор");

        Optional<String> first = box.take();
        Optional<String> second = box.take();

        assertThat(first).contains("пропелери");
        assertThat(second).contains("акумулятор");
        assertThat(box.isEmpty()).isTrue();
    }

    @Test
    void putAndTake_integers_worksWithBoxedNumbers() {
        Box<Integer> box = new Box<>();
        box.put(42);

        Optional<Integer> taken = box.take();

        assertThat(taken).contains(42);
    }

    @Test
    void putAndTake_customType_keepsSameInstance() {
        Box<Student> box = new Box<>();
        Student student = new Student("KB-001", "Іван", "Петренко", "КН-31", 90);
        box.put(student);

        Optional<Student> taken = box.take();

        assertThat(taken).containsSame(student);
    }

    @Test
    void putAndTake_listType_storesWholeList() {
        Box<List<Double>> box = new Box<>();
        box.put(List.of(1.5, 2.5));

        Optional<List<Double>> taken = box.take();

        assertThat(taken).contains(List.of(1.5, 2.5));
    }

    @Test
    void take_emptyBox_returnsEmptyOptional() {
        Box<String> box = new Box<>();

        Optional<String> taken = box.take();

        assertThat(taken).isEmpty();
    }

    @Test
    void peek_doesNotRemoveItem() {
        Box<String> box = new Box<>();
        box.put("goggles");

        Optional<String> peeked = box.peek();

        assertThat(peeked).contains("goggles");
        assertThat(box.size()).isEqualTo(1);
        assertThat(box.contains("goggles")).isTrue();
    }

    @Test
    void put_nullItem_throwsAndKeepsBoxEmpty() {
        Box<String> box = new Box<>();

        assertThatThrownBy(() -> box.put(null)).isInstanceOf(NullPointerException.class);

        assertThat(box.isEmpty()).isTrue();
    }

    @Test
    void put_fullBox_throwsIllegalState() {
        Box<Integer> box = new Box<>(2);
        box.put(1);
        box.put(2);

        assertThatThrownBy(() -> box.put(3))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("2");

        assertThat(box.isFull()).isTrue();
        assertThat(box.size()).isEqualTo(2);
    }

    @Test
    void newBox_nonPositiveCapacity_throwsIllegalArgument() {
        assertThatThrownBy(() -> new Box<String>(0)).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void takeAll_returnsEverythingAndEmptiesBox() {
        Box<Character> box = new Box<>(3);
        box.put('a');
        box.put('b');

        List<Character> all = box.takeAll();

        assertThat(all).containsExactly('a', 'b');
        assertThat(box.isEmpty()).isTrue();
        assertThat(box.capacity()).isEqualTo(3);
        assertThat(box).hasToString("Box[]");
    }
}
