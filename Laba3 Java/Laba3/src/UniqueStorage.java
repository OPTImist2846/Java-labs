import java.util.*;

public class UniqueStorage {
    public static <T> Set<T> getUniqueElements(List<T> inputList) {
        return new HashSet<>(inputList);
    }

    public static <T> Map<T, Integer> countOccurrences(List<T> inputList) {
        Map<T, Integer> counts = new HashMap<>();
        for (T item : inputList) {
            counts.put(item, counts.getOrDefault(item, 0) + 1);
        }
        return counts;
    }
}