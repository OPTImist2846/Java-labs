class InvalidPaymentException extends Exception {
  public InvalidPaymentException(String message) {
    super(message);
  }
}

public class Main {

  public static void processPayment(double amount) throws InvalidPaymentException {

    if (amount <= 0) {
      throw new InvalidPaymentException("Сума платежу не може бути нульовою або від'ємною! Передано: " + amount);
    }

    System.out.println("Платіж" + amount + " успішно проведено.");
  }
}