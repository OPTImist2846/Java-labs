package com.example.fpvfleet.lab3.generics;

import java.util.Comparator;
import java.util.Objects;

public final class Pair<F, S> {

    private final F first;
    private final S second;

    public Pair(F first, S second) {
        this.first = first;
        this.second = second;
    }

    public static <F, S> Pair<F, S> of(F first, S second) {
        return new Pair<>(first, second);
    }

    public F getFirst() {
        return first;
    }

    public S getSecond() {
        return second;
    }

    public Pair<S, F> swap() {
        return new Pair<>(second, first);
    }

    public boolean isSameAs(Pair<?, ?> other) {
        return other != null
                && Objects.equals(first, other.first)
                && Objects.equals(second, other.second);
    }

    public static <F extends Comparable<? super F>, S extends Comparable<? super S>>
    Comparator<Pair<F, S>> naturalOrder() {
        return Comparator.<Pair<F, S>, F>comparing(Pair::getFirst)
                .thenComparing(Pair::getSecond);
    }

    public static <F extends Comparable<? super F>, S extends Comparable<? super S>>
    int compare(Pair<F, S> left, Pair<F, S> right) {
        return Pair.<F, S>naturalOrder().compare(left, right);
    }

    @Override
    public boolean equals(Object o) {
        return o instanceof Pair<?, ?> other && isSameAs(other);
    }

    @Override
    public int hashCode() {
        return Objects.hash(first, second);
    }

    @Override
    public String toString() {
        return "(" + first + ", " + second + ")";
    }
}
