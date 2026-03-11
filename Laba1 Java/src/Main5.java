public class Main5 {
    public static void main(String[] args) {
        int[] numbers = {4, 2, 7, 8, 2, 3, 4, 1, 8};

        System.out.println("Повторювані елементи:");

        for (int i = 0; i < numbers.length; i++) {

            for (int j = i + 1; j < numbers.length; j++) {

                if (numbers[i] == numbers[j]) {
                    System.out.println(numbers[i]);
                    break;
                }
            }
        }
    }
}