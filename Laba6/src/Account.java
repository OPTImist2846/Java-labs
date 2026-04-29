import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class Account {
    private final String accountId;
    private final String clientName;

    private final AtomicInteger balance;
    private final ReentrantLock lock = new ReentrantLock();
    private final Condition sufficientFunds = lock.newCondition();

    public Account(String accountId, String clientName, int initialBalance) {
        this.accountId = accountId;
        this.clientName = clientName;
        this.balance = new AtomicInteger(initialBalance);
    }

    public void deposit(int amount) {
        lock.lock();
        try {
            balance.addAndGet(amount);
            System.out.println( clientName + " поповнив(ла) рахунок на " + amount + " грн. Баланс: " + balance.get());

            sufficientFunds.signalAll();
        } finally {
            lock.unlock();
        }
    }

    public void withdraw(int amount) throws InterruptedException {
        lock.lockInterruptibly();
        try {
            while (balance.get() < amount) {
                System.out.println(clientName + " очікує поповнення для зняття " + amount + " грн...");
                sufficientFunds.await();
            }
            balance.addAndGet(-amount);
            System.out.println(clientName + " зняв" + amount + " грн. Баланс: " + balance.get());
        } finally {
            lock.unlock();
        }
    }

    public ReentrantLock getLock() { return lock; }
    public String getAccountId() { return accountId; }
    public String getClientName() { return clientName; }
    public int getBalance() { return balance.get(); }
}