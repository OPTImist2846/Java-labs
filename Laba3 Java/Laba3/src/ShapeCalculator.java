import java.util.List;

public class ShapeCalculator {

    public static double calculateTotalArea(List<? extends Shape> shapes) {
        double totalArea = 0;

        for (Shape s : shapes) {
            totalArea += s.getArea();
        }

        return totalArea;
    }
}