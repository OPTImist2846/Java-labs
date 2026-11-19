package com.example.fpvfleet.lab3.wildcards;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class IntegerListsTest {

    @Test
    void addToList_integerList_addsOneToTen() {
        List<Integer> integers = new ArrayList<>();

        IntegerLists.addToList(integers);

        assertThat(integers).containsExactly(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);
    }

    @Test
    void addToList_numberListWithDoubles_appendsIntegersAfterExistingValues() {
        List<Number> numbers = new ArrayList<>(List.of(0.5, 2.5));

        IntegerLists.addToList(numbers);

        assertThat(numbers).hasSize(12).startsWith(0.5, 2.5).endsWith(9, 10);
    }

    @Test
    void addToList_objectList_acceptsIntegersAlongsideOtherTypes() {
        List<Object> objects = new ArrayList<>(List.of("start"));

        IntegerLists.addToList(objects, 1, 3);

        assertThat(objects).containsExactly("start", 1, 2, 3);
    }

    @Test
    void addToList_singleValueRange_addsExactlyOneNumber() {
        List<Integer> integers = new ArrayList<>();

        IntegerLists.addToList(integers, 5, 5);

        assertThat(integers).containsExactly(5);
    }

    @Test
    void addToList_reversedRange_throwsAndLeavesListUnchanged() {
        List<Integer> integers = new ArrayList<>();

        assertThatThrownBy(() -> IntegerLists.addToList(integers, 10, 1))
                .isInstanceOf(IllegalArgumentException.class);

        assertThat(integers).isEmpty();
    }

    @Test
    void addToList_nullList_throwsNullPointerException() {
        assertThatThrownBy(() -> IntegerLists.addToList(null)).isInstanceOf(NullPointerException.class);
    }
}
