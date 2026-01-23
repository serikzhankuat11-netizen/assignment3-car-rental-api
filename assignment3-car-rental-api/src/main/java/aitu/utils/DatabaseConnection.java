
package aitu.utils;

import aitu.exception.DatabaseOperationException;

import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
    private static final String DB_URL = "jdbc:sqlite:data/car_rental.db";

    private DatabaseConnection() {}

    public static Connection getConnection() {
        try {

            Files.createDirectories(Path.of("data"));

            return DriverManager.getConnection(DB_URL);
        } catch (SQLException e) {
            throw new DatabaseOperationException("Cannot connect to database: " + DB_URL, e);
        } catch (Exception e) {
            throw new DatabaseOperationException("Cannot create data directory for DB", e);
        }
    }
}
