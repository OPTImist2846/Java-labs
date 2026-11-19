package com.example.fpvfleet.lab3.unique;

import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.Assertions.entry;

class UniqueElementsTest {

    @Test
    void unique_listWithDuplicates_keepsFirstOccurrenceOrder() {
        List<String> drones = List.of("Nazgul", "Cinewhoop", "Nazgul", "Mobula", "Cinewhoop");

        Set<String> result = UniqueElements.unique(drones);

        assertThat(result).containsExactly("Nazgul", "Cinewhoop", "Mobula");
    }

    @Test
    void unique_emptyList_returnsEmptySet() {
        Set<Integer> result = UniqueElements.unique(List.of());

        assertThat(result).isEmpty();
    }

    @Test
    void unique_listWithNulls_keepsSingleNull() {
        Set<String> result = UniqueElements.unique(Arrays.asList("a", null, "a", null));

        assertThat(result).containsExactly("a", null);
    }

    @Test
    void unique_nullList_throwsNullPointerException() {
        assertThatThrownBy(() -> UniqueElements.unique(null)).isInstanceOf(NullPointerException.class);
    }

    @Test
    void countOccurrences_listWithDuplicates_countsEachElement() {
        List<Integer> grades = List.of(5, 4, 5, 3, 5, 4);

        Map<Integer, Integer> result = UniqueElements.countOccurrences(grades);

        assertThat(result).containsExactly(entry(5, 3), entry(4, 2), entry(3, 1));
    }

    @Test
    void countOccurrences_emptyList_returnsEmptyMap() {
        Map<String, Integer> result = UniqueElements.countOccurrences(List.of());

        assertThat(result).isEmpty();
    }

    @Test
    void countOccurrences_nullList_throwsNullPointerException() {
        assertThatThrownBy(() -> UniqueElements.countOccurrences(null)).isInstanceOf(NullPointerException.class);
    }
}
