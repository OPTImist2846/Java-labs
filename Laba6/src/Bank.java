import java.util.Map;
import java.util.concurrent.Semaphore;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class Bank {
    private final Map<String, Account> accounts;

    private final Semaphore transactionLimit = new Semaphore(2);

    private final ReentrantReadWriteLock auditLock = new ReentrantReadWriteLock();

    public Bank(Map<String, Account> accounts) {
        this.accounts = accounts;
    }

    public void transfer(String fromId, String toId, int amount) throws InterruptedException {
        Account fromAccount = accounts.get(fromId);
        Account toAccount = accounts.get(toId);

        transactionLimit.acquire();
        try {
            boolean transferred = false;

            while (!transferred) {
                if (fromAccount.getLock().tryLock()) {
                    try {
                        if (toAccount.getLock().tryLock()) {
                            try {
                                if (fromAccount.getBalance() >= amount) {
                                    fromAccount.deposit(-amount);
                                    toAccount.deposit(amount);
                                    System.out.println("Переказ " + amount + " грн: "
                                            + fromAccount.getClientName() + toAccount.getClientName());
                                    transferred = true;
                                } else {
                                    System.out.println("Недостатньо коштів для переказу у " + fromAccount.getClientName());
                                    transferred = true;
                                }
                            } finally {
                                toAccount.getLock().unlock();
                            }
                        }
                    } finally {
                        fromAccount.getLock().unlock();
                    }
                }
                if (!transferred) {
                    Thread.sleep(50);
                }
            }
        } finally {
            transactionLimit.release();
        }
    }

    public void printTotalBankBalance() {
        auditLock.readLock().lock();
        try {
            int total = accounts.values().stream().mapToInt(Account::getBalance).sum();
            System.out.println("Загальний капітал банку: " + total + " грн.");
        } finally {
            auditLock.readLock().unlock();
        }
    }
}