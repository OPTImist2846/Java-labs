package com.example.fpvfleet.lab3.shapes;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.Assertions.within;

class ShapeCalculatorTest {

    private static final double EPS = 1e-9;

    @Test
    void getArea_circle_usesPiRSquared() {
        Circle circle = new Circle(2);

        double area = circle.getArea();

        assertThat(area).isCloseTo(4 * Math.PI, within(EPS));
        assertThat(circle.getRadius()).isEqualTo(2);
    }

    @Test
    void getArea_rectangle_multipliesSides() {
        Rectangle rectangle = new Rectangle(3, 4.5);

        double area = rectangle.getArea();

        assertThat(area).isEqualTo(13.5);
        assertThat(rectangle.getWidth()).isEqualTo(3);
        assertThat(rectangle.getHeight()).isEqualTo(4.5);
    }

    @Test
    void calculateTotalArea_listOfShapes_sumsMixedShapes() {
        List<Shape> shapes = List.of(new Circle(1), new Rectangle(2, 3));

        double total = ShapeCalculator.calculateTotalArea(shapes);

        assertThat(total).isCloseTo(Math.PI + 6, within(EPS));
    }

    @Test
    void calculateTotalArea_listOfCircles_acceptedThroughUpperBound() {
        List<Circle> circles = List.of(new Circle(1), new Circle(1));

        double total = ShapeCalculator.calculateTotalArea(circles);

        assertThat(total).isCloseTo(2 * Math.PI, within(EPS));
    }

    @Test
    void calculateTotalArea_listOfRectangles_acceptedThroughUpperBound() {
        List<Rectangle> rectangles = List.of(new Rectangle(1, 2), new Rectangle(3, 4));

        double total = ShapeCalculator.calculateTotalArea(rectangles);

        assertThat(total).isEqualTo(14.0);
    }

    @Test
    void calculateTotalArea_emptyList_returnsZero() {
        double total = ShapeCalculator.calculateTotalArea(List.of());

        assertThat(total).isZero();
    }

    @Test
    void calculateTotalArea_listWithNull_throwsNullPointerException() {
        List<Shape> shapes = Arrays.asList(new Circle(1), null);

        assertThatThrownBy(() -> ShapeCalculator.calculateTotalArea(shapes))
                .isInstanceOf(NullPointerException.class);
    }

    @ParameterizedTest
    @ValueSource(doubles = {0, -1, Double.NaN, Double.POSITIVE_INFINITY})
    void newCircle_invalidRadius_throwsIllegalArgument(double radius) {
        assertThatThrownBy(() -> new Circle(radius))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Радіус");
    }

    @Test
    void newRectangle_nonPositiveHeight_throwsIllegalArgument() {
        assertThatThrownBy(() -> new Rectangle(2, 0))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Висота");
    }

    @Test
    void toString_describesShapeDimensions() {
        assertThat(new Circle(1.5)).hasToString("Circle(r=1.5)");
        assertThat(new Rectangle(2, 3)).hasToString("Rectangle(2.0x3.0)");
    }
}
