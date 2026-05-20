import com.mongodb.client.*;
import com.mongodb.client.model.*;
import org.bson.Document;
import org.bson.conversions.Bson;

import java.util.Arrays;

public class MongoLabApp {

    // ТУТ ВСТАВ СВІЙ РЯДОК ПІДКЛЮЧЕННЯ З ATLAS (з паролем)
    private static final String CONNECTION_URI = "mongodb+srv://OPTImist:ТВІЙ_ПАРОЛЬ@cluster0.tmr3l6f.mongodb.net/";
    private static final String DATABASE_NAME = "sales_department_db";

    public static void main(String[] args) {
        // Підключаємось до кластера
        try (MongoClient mongoClient = MongoClients.create(CONNECTION_URI)) {
            MongoDatabase database = mongoClient.getDatabase(DATABASE_NAME);
            System.out.println("✅ Успішно підключено до бази: " + database.getName() + "\n");

            // Виконуємо наші методи
            runSelectors(database);
            runAggregations(database);

        } catch (Exception e) {
            System.err.println("Помилка підключення: " + e.getMessage());
        }
    }

    /**
     * 1. СЕЛЕКТОРИ (Вибірка та видалення)
     */
    private static void runSelectors(MongoDatabase db) {
        System.out.println("================ СЕЛЕКТОРИ ================");
        MongoCollection<Document> products = db.getCollection("products");
        MongoCollection<Document> employees = db.getCollection("employees");

        // Селектор 1: Знайти товари, ціна яких >= 200 і < 1000
        System.out.println("\n--- Товари з ціною від 200 до 1000 ---");
        Bson priceFilter = Filters.and(Filters.gte("price", 200), Filters.lt("price", 1000));

        for (Document doc : products.find(priceFilter)) {
            System.out.println(doc.toJson());
        }

        // Селектор 2: Працівники, які є Менеджерами або Старшими менеджерами
        System.out.println("\n--- Менеджери та Старші менеджери ---");
        Bson positionFilter = Filters.or(
                Filters.eq("details.position", "Менеджер"),
                Filters.eq("details.position", "Старший менеджер")
        );
        for (Document doc : employees.find(positionFilter)) {
            System.out.println(doc.toJson());
        }

        // Видалення: Видалити товари, яких немає на складі (stock_balance = 0)
        System.out.println("\n--- Видалення товарів із залишком 0 ---");
        products.deleteMany(Filters.eq("stock_balance", 0));
        System.out.println("Операція видалення виконана.");
    }

    /**
     * 2. АГРЕГАЦІЇ
     */
    private static void runAggregations(MongoDatabase db) {
        System.out.println("\n================ АГРЕГАЦІЇ ================");
        MongoCollection<Document> invoices = db.getCollection("invoices");
        MongoCollection<Document> products = db.getCollection("products");

        // Агрегація 1: Топ-3 товари, які продавалися найбільше
        System.out.println("\n--- Топ-3 продаваних товари ---");
        invoices.aggregate(Arrays.asList(
                Aggregates.unwind("$items"), // Розгортаємо масив
                Aggregates.group("$items.product_id", Accumulators.sum("total_sold", "$items.quantity")), // Групуємо
                Aggregates.lookup("products", "_id", "_id", "product_info"), // Приєднуємо колекцію products
                Aggregates.unwind("$product_info"),
                Aggregates.sort(Sorts.descending("total_sold")), // Сортуємо
                Aggregates.limit(3) // Залишаємо топ 3
        )).forEach(doc -> System.out.println(doc.toJson()));

        // Агрегація 2: Статистика цін за категоріями (одиницями виміру)
        System.out.println("\n--- Статистика цін за одиницями виміру ---");
        products.aggregate(Arrays.asList(
                Aggregates.match(Filters.gt("stock_balance", 0)),
                Aggregates.group("$metric",
                        Accumulators.avg("avg_price", "$price"),
                        Accumulators.max("max_price", "$price")),
                Aggregates.sort(Sorts.descending("avg_price")),
                Aggregates.project(Projections.fields(
                        Projections.excludeId(),
                        Projections.computed("metric", "$_id"),
                        Projections.include("avg_price", "max_price")))
        )).forEach(doc -> System.out.println(doc.toJson()));

        // Агрегація 3: Найкращі працівники за кількістю проданих товарів (> 2 шт)
        System.out.println("\n--- Працівники, що продали більше 2 товарів ---");
        invoices.aggregate(Arrays.asList(
                Aggregates.unwind("$items"),
                Aggregates.group("$employee_id", Accumulators.sum("items_sold", "$items.quantity")),
                Aggregates.match(Filters.gt("items_sold", 2)),
                Aggregates.lookup("employees", "_id", "_id", "emp_info"),
                Aggregates.unwind("$emp_info"),
                Aggregates.sort(Sorts.descending("items_sold")),
                Aggregates.project(Projections.fields(
                        Projections.excludeId(),
                        Projections.include("emp_info.full_name", "items_sold")
                ))
        )).forEach(doc -> System.out.println(doc.toJson()));
    }
}