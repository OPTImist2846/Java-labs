package com.example.fpvfleet.lab3.generics;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class Box<T> {

    private final Deque<T> items = new ArrayDeque<>();
    private final int capacity;

    public Box() {
        this(Integer.MAX_VALUE);
    }

    public Box(int capacity) {
        if (capacity <= 0) {
            throw new IllegalArgumentException("Місткість ящика має бути більшою за 0");
        }
        this.capacity = capacity;
    }

    public void put(T item) {
        Objects.requireNonNull(item, "Не можна покласти в ящик null");
        if (isFull()) {
            throw new IllegalStateException("Ящик заповнений, місткість " + capacity);
        }
        items.addLast(item);
    }

    public Optional<T> take() {
        return Optional.ofNullable(items.pollFirst());
    }

    public Optional<T> peek() {
        return Optional.ofNullable(items.peekFirst());
    }

    public List<T> takeAll() {
        List<T> all = List.copyOf(items);
        items.clear();
        return all;
    }

    public boolean contains(T item) {
        return items.contains(item);
    }

    public int size() {
        return items.size();
    }

    public int capacity() {
        return capacity;
    }

    public boolean isEmpty() {
        return items.isEmpty();
    }

    public boolean isFull() {
        return items.size() >= capacity;
    }

    @Override
    public String toString() {
        return "Box" + items;
    }
}
