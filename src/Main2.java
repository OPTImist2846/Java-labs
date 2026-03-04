public class Main2 {
    public static void main(String[] args) {
    int number = 29;
    boolean isPrime = true;

    if (number <= 1) {
        isPrime = false;
    } else {
        for (int i = 2; i <= Math.sqrt(number); i++) {

            if (number % i == 0) {
                isPrime = false;
                break;
            }
        }
    }

    if (isPrime) {
        System.out.println(number + " є простим числом.");
    } else {
        System.out.println(number + " не є простим числом.");
    }
}
}
