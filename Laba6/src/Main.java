import java.util.HashMap;
import java.util.Map;

public class Main {

    static class BankStatus {
        private boolean isOpen = false;

        // Метод очікування
        public synchronized void waitForOpen() throws InterruptedException {
            while (!isOpen) {
                System.out.println(Thread.currentThread().getName() + " чекає на відкриття банку...");
                wait();
            }
        }

        // Будить всі потоки
        public synchronized void openBank() {
            isOpen = true;
            System.out.println(" БАНК ВІДКРИТО! Починаєм роботу.");
            notifyAll();
        }
    }

    public static void main(String[] args) throws InterruptedException {
        // Створення рахунків
        Account acc1 = new Account("1", "Клієнт А", 5000);
        Account acc2 = new Account("2", "Клієнт Б", 3000);

        Map<String, Account> accountMap = new HashMap<>();
        accountMap.put(acc1.getAccountId(), acc1);
        accountMap.put(acc2.getAccountId(), acc2);

        Bank bank = new Bank(accountMap);
        BankStatus bankStatus = new BankStatus();

        // Створення потоків
        Runnable clientTask1 = () -> {
            try {
                bankStatus.waitForOpen();

                acc1.withdraw(1000);
                bank.transfer("1", "2", 1500);

            } catch (InterruptedException e) {
                // Примусова зупинка
                System.out.println("Потік " + Thread.currentThread().getName() + " був примусово перерваний!");
            }
        };

        Runnable clientTask2 = () -> {
            try {
                bankStatus.waitForOpen();

                acc2.deposit(2000);
                bank.transfer("2", "1", 500);

            } catch (InterruptedException e) {
                System.out.println("Потік " + Thread.currentThread().getName() + " був примусово перерваний!");
            }
        };

        Thread thread1 = new Thread(clientTask1);
        Thread thread2 = new Thread(clientTask2);

        thread1.setName("Потік-Клієнт-А");
        thread2.setName("Потік-Клієнт-Б");

        thread1.setPriority(Thread.MAX_PRIORITY);
        thread2.setPriority(Thread.NORM_PRIORITY);

        // Фоновий потік для аудиту банку
        Thread auditThread = new Thread(() -> {
            try {
                while (!Thread.currentThread().isInterrupted()) {
                    Thread.sleep(50);
                    bank.printTotalBankBalance();
                }
            } catch (InterruptedException e) {
                System.out.println(" Фоновий аудит зупинено (interrupt).");
            }
        });
        auditThread.setName("Потік-Аудит");
        auditThread.setDaemon(true);

        // Запускаємо всі потоки
        thread1.start();
        thread2.start();
        auditThread.start();

        // Імітуємо підготовку банку до відкриття
        Thread.sleep(1000);

        // Відкриваємо банк
        bankStatus.openBank();

        // Main чекає завершення роботи клієнтів
        thread1.join();
        thread2.join();

        // Примусова зупинка потоків
        auditThread.interrupt();

        System.out.println(" Робочий день банку завершено.");
    }
}