package com.example.fpvfleet.lab3.shapes;

public abstract class Shape {

    public abstract double getArea();

    protected static double requirePositive(double value, String name) {
        if (!(value > 0) || Double.isInfinite(value)) {
            throw new IllegalArgumentException(name + " має бути додатним числом");
        }
        return value;
    }
}
