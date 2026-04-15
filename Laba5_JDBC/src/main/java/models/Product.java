package models;

public class Product {
    private int id;
    private String productName;
    private double price;
    private int stockBalance;
    private int metricId;

    public Product() {}

    public Product(int id, String productName, double price, int stockBalance, int metricId) {
        this.id = id;
        this.productName = productName;
        this.price = price;
        this.stockBalance = stockBalance;
        this.metricId = metricId;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getProductName() { return productName; }
    public void setProductName(String productName) { this.productName = productName; }

    public double getPrice() { return price; }
    public void setPrice(double price) { this.price = price; }

    public int getStockBalance() { return stockBalance; }
    public void setStockBalance(int stockBalance) { this.stockBalance = stockBalance; }

    public int getMetricId() { return metricId; }
    public void setMetricId(int metricId) { this.metricId = metricId; }
}