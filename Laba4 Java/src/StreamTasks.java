import java.util.*;

public class StreamTasks {

    public static Optional<String> task1(List<String> list) {
        return Optional.of(list.stream()
                .filter(s -> s.startsWith("X") && s.length() > 5)
                .findFirst()
                .orElse("Default"));
    }


}