import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {

    // Өз DB-ңе қарай өзгертесің:
    private static final String URL = "jdbc:postgresql://localhost:5432/car_rental_db";
    private static final String USER = "postgres";
    private static final String PASS = "25032008";

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASS);
    }
}
