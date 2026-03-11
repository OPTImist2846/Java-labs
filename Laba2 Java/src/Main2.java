import java.util.LinkedList;
import java.util.Iterator;

class Payment {
    private int id;
    private double amount;

    public Payment(int id, double amount) {
        this.id = id;
        this.amount = amount;
    }

    public int getId() {
        return id;
    }

    public double getAmount() {
        return amount;
    }

    @Override
    public String toString() {
        return "Платіж {ID=" + id + ", сума=" + amount + "}";
    }
}

public class Main2 {


    public static double calculateTotalSum(LinkedList<Payment> payments) {
        double total = 0;
        for (Payment p : payments) {
            total += p.getAmount();
        }
        return total;
    }

    public static void removePaymentById(LinkedList<Payment> payments, int searchId) {
        Iterator<Payment> iterator = payments.iterator();
        boolean isFound = false;

        while (iterator.hasNext()) {
            Payment currentPayment = iterator.next();
            if (currentPayment.getId() == searchId) {
                iterator.remove();
                System.out.println("Система: Платіж з ID " + searchId + " успішно видалено.");
                isFound = true;
                break;
            }
        }

        if (!isFound) {
            System.out.println("Система: Платіж з ID " + searchId + " не знайдено.");
        }
    }

     public static void main(String[] args) {

        LinkedList<Payment> myPayments = new LinkedList<>();

        myPayments.add(new Payment(1, 150.50));
        myPayments.add(new Payment(2, 300.00));
        myPayments.add(new Payment(3, 49.99));

        System.out.println("Початковий список платежів");
        for (Payment p : myPayments) {
            System.out.println(p);
        }

        System.out.println("\nЗагальна сума всіх платежів: " + calculateTotalSum(myPayments));

        System.out.println("\nСпроба видалення платежу з ID 2");
        removePaymentById(myPayments, 2);

        System.out.println("\nСписок платежів після видалення");
        for (Payment p : myPayments) {
            System.out.println(p);
        }

        System.out.println("\nНова загальна сума: " + calculateTotalSum(myPayments));
    }
}