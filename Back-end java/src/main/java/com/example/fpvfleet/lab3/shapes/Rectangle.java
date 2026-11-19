package com.example.fpvfleet.lab3.shapes;

public class Rectangle extends Shape {

    private final double width;
    private final double height;

    public Rectangle(double width, double height) {
        this.width = requirePositive(width, "Ширина");
        this.height = requirePositive(height, "Висота");
    }

    public double getWidth() {
        return width;
    }

    public double getHeight() {
        return height;
    }

    @Override
    public double getArea() {
        return width * height;
    }

    @Override
    public String toString() {
        return "Rectangle(" + width + "x" + height + ")";
    }
}
