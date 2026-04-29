import java.util.HashMap;
import java.util.Map;

public class Main {

    // Допоміжний об'єкт для демонстрації wait() та notifyAll() (Вимоги 13, 16)
    static class BankStatus {
        private boolean isOpen = false;

        // Метод, де потоки чекають на відкриття банку
        public synchronized void waitForOpen() throws InterruptedException {
            while (!isOpen) {
                System.out.println(Thread.currentThread().getName() + " чекає на відкриття банку...");
                wait(); // 16. wait(): Потік засинає і віддає монітор
            }
        }

        // Метод, який будить всі потоки
        public synchronized void openBank() {
            isOpen = true;
            System.out.println(" БАНК ВІДКРИТО! Починаєм роботу.");
            notifyAll(); // 16. notifyAll(): Будить всі потоки, які викликали wait()
        }
    }

    public static void main(String[] args) throws InterruptedException {
        // Створюємо рахунки
        Account acc1 = new Account("1", "Клієнт А", 5000);
        Account acc2 = new Account("2", "Клієнт Б", 3000);

        Map<String, Account> accountMap = new HashMap<>();
        accountMap.put(acc1.getAccountId(), acc1);
        accountMap.put(acc2.getAccountId(), acc2);

        Bank bank = new Bank(accountMap);
        BankStatus bankStatus = new BankStatus();

        // 1. Створення потоків (через інтерфейс Runnable)
        Runnable clientTask1 = () -> {
            try {
                bankStatus.waitForOpen(); // Чекаємо, поки банк відкриється

                acc1.withdraw(1000);
                bank.transfer("1", "2", 1500);

            } catch (InterruptedException e) {
                // 2. Обробка примусової зупинки
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

        // Створюємо об'єкти Thread на основі Runnable
        Thread thread1 = new Thread(clientTask1);
        Thread thread2 = new Thread(clientTask2);

        // 4. Властивості потоків (назва, пріоритет)
        thread1.setName("Потік-Клієнт-А");
        thread2.setName("Потік-Клієнт-Б");

        thread1.setPriority(Thread.MAX_PRIORITY); // Найвищий пріоритет (10)
        thread2.setPriority(Thread.NORM_PRIORITY); // Стандартний пріоритет (5)

        // Фоновий потік для аудиту банку
        Thread auditThread = new Thread(() -> {
            try {
                while (!Thread.currentThread().isInterrupted()) {
                    Thread.sleep(50); // Робимо аудит кожні 50 мс
                    bank.printTotalBankBalance();
                }
            } catch (InterruptedException e) {
                System.out.println(" Фоновий аудит зупинено (interrupt).");
            }
        });
        auditThread.setName("Потік-Аудит");
        auditThread.setDaemon(true); // Робимо його фоновим

        // Запускаємо всі потоки (вони зараз застрягнуть на wait(), бо банк зачинено)
        thread1.start();
        thread2.start();
        auditThread.start();

        // Імітуємо підготовку банку до відкриття
        Thread.sleep(1000);

        // Відкриваємо банк (це викличе notifyAll() і розбудить Клієнтів А і Б)
        bankStatus.openBank();

        // 5. Координація потоків: Головний потік (Main) чекає завершення роботи клієнтів
        thread1.join();
        thread2.join();

        // 2. Примусова зупинка потоків: Зупиняємо аудит, бо клієнти вже все закінчили
        auditThread.interrupt();

        System.out.println(" Робочий день банку завершено.");
    }
}