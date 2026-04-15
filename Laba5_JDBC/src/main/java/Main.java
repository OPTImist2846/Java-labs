import service.AuthService;
import service.ProductService;
import models.Product;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        ProductService productService = new ProductService();

        System.out.println("ТЕСТУВАННЯ CRUD");

        // 1. CREATE
        // productService.addProduct("Ноутбук Ігровий", 45000.50, 10, 1);
        //productService.addProduct("Мишка бездротова", 800.00, 50, 1);
        //System.out.println("✅ Товари додано в базу.");

        // 2. READ
        System.out.println("\nСписок всіх товарів");
        List<Product> products = productService.getAllProducts();
        for (Product p : products) {
            System.out.println("ID: " + p.getId() + " | Назва: " + p.getProductName() + " | Ціна: " + p.getPrice());
        }

        // 3. UPDATE
        if (productService.updateProductPrice(6, 600.00)) {
            System.out.println("\n✅ Ціну товару з ID 6 успішно оновлено (Знижка!).");
        }

        // 4. SEARCH
        productService.searchProductByName("Миш");

        // 5. DELETE
        // productService.deleteProduct(2);
        // System.out.println("\n🗑️ Товар видалено.");
    }
}