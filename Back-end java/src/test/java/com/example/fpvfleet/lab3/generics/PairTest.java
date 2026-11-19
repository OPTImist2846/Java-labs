package com.example.fpvfleet.lab3.generics;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class PairTest {

    @Test
    void pairIntegerString_storesBothValues() {
        Pair<Integer, String> pair = Pair.of(1, "Nazgul");

        assertThat(pair.getFirst()).isEqualTo(1);
        assertThat(pair.getSecond()).isEqualTo("Nazgul");
        assertThat(pair).hasToString("(1, Nazgul)");
    }

    @Test
    void pairStringList_storesListAsSecondValue() {
        Pair<String, List<Integer>> pair = Pair.of("оцінки", List.of(5, 4, 5));

        assertThat(pair.getSecond()).containsExactly(5, 4, 5);
    }

    @Test
    void isSameAs_equalValues_returnsTrue() {
        Pair<String, List<Integer>> left = Pair.of("оцінки", List.of(5, 4));
        Pair<String, List<Integer>> right = Pair.of("оцінки", new ArrayList<>(List.of(5, 4)));

        boolean same = left.isSameAs(right);

        assertThat(same).isTrue();
        assertThat(left).isEqualTo(right).hasSameHashCodeAs(right);
    }

    @Test
    void isSameAs_differentSecondValue_returnsFalse() {
        Pair<Integer, String> left = Pair.of(1, "a");
        Pair<Integer, String> right = Pair.of(1, "b");

        assertThat(left.isSameAs(right)).isFalse();
        assertThat(left).isNotEqualTo(right);
    }

    @Test
    void isSameAs_pairsOfDifferentTypes_returnsFalse() {
        Pair<Integer, String> numbers = Pair.of(1, "1");
        Pair<String, Integer> swapped = Pair.of("1", 1);

        assertThat(numbers.isSameAs(swapped)).isFalse();
    }

    @Test
    void isSameAs_null_returnsFalse() {
        assertThat(Pair.of(1, 2).isSameAs(null)).isFalse();
    }

    @Test
    void isSameAs_nullComponents_comparesSafely() {
        Pair<String, String> left = Pair.of(null, "x");
        Pair<String, String> right = new Pair<>(null, "x");

        assertThat(left.isSameAs(right)).isTrue();
    }

    @Test
    void compare_firstValuesDiffer_ordersByFirst() {
        int result = Pair.compare(Pair.of(1, "z"), Pair.of(2, "a"));

        assertThat(result).isNegative();
    }

    @Test
    void compare_firstValuesEqual_ordersBySecond() {
        int result = Pair.compare(Pair.of(1, "b"), Pair.of(1, "a"));

        assertThat(result).isPositive();
    }

    @Test
    void compare_equalPairs_returnsZero() {
        assertThat(Pair.compare(Pair.of("a", 1), Pair.of("a", 1))).isZero();
    }

    @Test
    void naturalOrder_sortsListOfPairs() {
        List<Pair<Integer, String>> pairs = new ArrayList<>(List.of(Pair.of(2, "b"), Pair.of(1, "z"), Pair.of(1, "a")));

        pairs.sort(Pair.naturalOrder());

        assertThat(pairs).containsExactly(Pair.of(1, "a"), Pair.of(1, "z"), Pair.of(2, "b"));
    }

    @Test
    void swap_returnsPairWithExchangedTypes() {
        Pair<String, Integer> swapped = Pair.of(7, "seven").swap();

        assertThat(swapped).isEqualTo(Pair.of("seven", 7));
    }
}
