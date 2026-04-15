package database;

import config.DatabaseConfig;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(
                DatabaseConfig.getDbUrl(localhost:3306/oleksii_fysiuk),      // Адреса (наприклад, localhost:3306/oleksii_fysiuk)
                DatabaseConfig.getDbUser(root),     // Твій логін у MySQL (наприклад, root)
                DatabaseConfig.getDbPassword(1Qaz2Wsx3_2)  // Твій пароль у MySQL
        );
    }
}