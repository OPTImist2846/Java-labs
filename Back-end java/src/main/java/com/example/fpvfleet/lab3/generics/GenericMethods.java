package com.example.fpvfleet.lab3.generics;

public final class GenericMethods {

    private GenericMethods() {
    }

    public static <T extends Comparable<? super T>> T findMax(T[] array) {
        if (array == null || array.length == 0) {
            throw new IllegalArgumentException("Масив не може бути порожнім");
        }
        T max = requireElement(array, 0);
        for (int i = 1; i < array.length; i++) {
            T candidate = requireElement(array, i);
            if (candidate.compareTo(max) > 0) {
                max = candidate;
            }
        }
        return max;
    }

    private static <T> T requireElement(T[] array, int index) {
        if (array[index] == null) {
            throw new IllegalArgumentException("Елемент масиву з індексом " + index + " дорівнює null");
        }
        return array[index];
    }
}
