public class Transaction {
    private double amount;
    private String category;

    public Transaction(double amount, String category) { this.amount = amount; this.category = category; }
    public double getAmount() { return amount; }
    public String getCategory() { return category; }
}