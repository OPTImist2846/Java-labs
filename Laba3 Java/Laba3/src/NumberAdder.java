import java.util.List;

public class NumberAdder {

    public static void addNumbers(List<? super Integer> list) {
        for (int i = 1; i <= 10; i++) {
            list.add(i);
        }
        System.out.println("Список після додавання: " + list);
    }
}