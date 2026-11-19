package com.example.fpvfleet.lab3.shapes;

import java.util.List;
import java.util.Objects;

public final class ShapeCalculator {

    private ShapeCalculator() {
    }

    public static double calculateTotalArea(List<? extends Shape> shapes) {
        Objects.requireNonNull(shapes, "shapes");
        double total = 0;
        for (Shape shape : shapes) {
            total += Objects.requireNonNull(shape, "Список фігур містить null").getArea();
        }
        return total;
    }
}
