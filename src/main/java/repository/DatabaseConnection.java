package repository;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public final class DatabaseConnection {

    private static final String URL =
            getRequiredEnvironmentVariable("DB_URL");

    private static final String USER =
            getRequiredEnvironmentVariable("DB_USER");

    private static final String PASSWORD =
            getRequiredEnvironmentVariable("DB_PASSWORD");

    private static Connection connection;

    private DatabaseConnection() {
        throw new IllegalStateException(
                "DatabaseConnection cannot be instantiated"
        );
    }

    public static synchronized Connection getConnection()
            throws SQLException {

        if (connection == null || connection.isClosed()) {
            connection = DriverManager.getConnection(
                    URL,
                    USER,
                    PASSWORD
            );
        }

        return connection;
    }

    public static synchronized void closeConnection() {
        if (connection != null) {
            try {
                connection.close();
                connection = null;
            } catch (SQLException exception) {
                throw new RuntimeException(
                        "Ошибка при закрытии соединения",
                        exception
                );
            }
        }
    }

    private static String getRequiredEnvironmentVariable(String name) {
        String value = System.getenv(name);

        if (value == null || value.isBlank()) {
            throw new IllegalStateException(
                    "Не задана переменная окружения: " + name
            );
        }

        return value;
    }
}
