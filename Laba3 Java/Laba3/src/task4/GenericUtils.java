package task4;

public class GenericUtils {

    public static <T extends Comparable<T>> T findMax(T[] array) {

        if (array == null || array.length == 0) {
            return null;
        }

        T max = array[0];

        for (int i = 1; i < array.length; i++) {

            if (array[i].compareTo(max) > 0) {
                max = array[i]; // Оновлюємо максимум
            }
        }

        return max;
    }
}