package com.example.fpvfleet.lab3.shapes;

public class Circle extends Shape {

    private final double radius;

    public Circle(double radius) {
        this.radius = requirePositive(radius, "Радіус");
    }

    public double getRadius() {
        return radius;
    }

    @Override
    public double getArea() {
        return Math.PI * radius * radius;
    }

    @Override
    public String toString() {
        return "Circle(r=" + radius + ")";
    }
}
