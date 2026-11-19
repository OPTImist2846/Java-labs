package com.example.fpvfleet.lab3.wildcards;

import java.util.List;
import java.util.Objects;

public final class IntegerLists {

    private IntegerLists() {
    }

    public static void addToList(List<? super Integer> list) {
        addToList(list, 1, 10);
    }

    public static void addToList(List<? super Integer> list, int from, int to) {
        Objects.requireNonNull(list, "list");
        if (from > to) {
            throw new IllegalArgumentException("Початок діапазону " + from + " більший за кінець " + to);
        }
        for (int value = from; value <= to; value++) {
            list.add(value);
        }
    }
}
