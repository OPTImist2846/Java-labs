package com.example.fpvfleet.lab3.unique;

import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

public final class UniqueElements {

    private UniqueElements() {
    }

    public static <T> Set<T> unique(List<? extends T> items) {
        Objects.requireNonNull(items, "items");
        return new LinkedHashSet<>(items);
    }

    public static <T> Map<T, Integer> countOccurrences(List<? extends T> items) {
        Objects.requireNonNull(items, "items");
        Map<T, Integer> counts = new LinkedHashMap<>();
        for (T item : items) {
            counts.merge(item, 1, Integer::sum);
        }
        return counts;
    }
}
