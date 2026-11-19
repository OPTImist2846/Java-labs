package com.example.fpvfleet.lab3.generics;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class GenericMethodsTest {

    @Test
    void findMax_integers_returnsLargest() {
        Integer[] values = {3, -7, 42, 0, 41};

        Integer max = GenericMethods.findMax(values);

        assertThat(max).isEqualTo(42);
    }

    @Test
    void findMax_doubles_handlesNegativeNumbers() {
        Double[] values = {-1.5, -0.25, -3.0};

        Double max = GenericMethods.findMax(values);

        assertThat(max).isEqualTo(-0.25);
    }

    @Test
    void findMax_characters_usesUnicodeOrder() {
        Character[] values = {'b', 'Z', 'a', 'y'};

        Character max = GenericMethods.findMax(values);

        assertThat(max).isEqualTo('y');
    }

    @Test
    void findMax_strings_usesLexicographicOrder() {
        String[] values = {"Mobula", "Nazgul", "Cinewhoop"};

        String max = GenericMethods.findMax(values);

        assertThat(max).isEqualTo("Nazgul");
    }

    @Test
    void findMax_typeComparableViaSuperclass_isSupported() {
        LocalDate[] dates = {LocalDate.of(2026, 9, 1), LocalDate.of(2026, 12, 31), LocalDate.of(2026, 11, 19)};

        LocalDate max = GenericMethods.findMax(dates);

        assertThat(max).isEqualTo(LocalDate.of(2026, 12, 31));
    }

    @Test
    void findMax_singleElement_returnsThatElement() {
        Integer max = GenericMethods.findMax(new Integer[]{7});

        assertThat(max).isEqualTo(7);
    }

    @Test
    void findMax_emptyArray_throwsIllegalArgument() {
        assertThatThrownBy(() -> GenericMethods.findMax(new Integer[0]))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void findMax_nullArray_throwsIllegalArgument() {
        assertThatThrownBy(() -> GenericMethods.findMax((String[]) null))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void findMax_nullElement_throwsIllegalArgumentWithIndex() {
        assertThatThrownBy(() -> GenericMethods.findMax(new Integer[]{1, null, 3}))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("1");
    }
}
