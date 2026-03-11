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

  public static void main(String[] args) {
    System.out.println("Система обробки платежів");

    try {
      processPayment(500.0);
      processPayment(-50.0);
      processPayment(100.0);

    } catch (InvalidPaymentException e) {
      System.out.println("ПОМИЛКА: " + e.getMessage());
    }
  }
}