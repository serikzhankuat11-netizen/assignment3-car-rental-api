package aitu.utils;

import aitu.exception.DatabaseOperationException;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.stream.Collectors;

public class DbInit {

    private DbInit(){}

    /**
     * Initializes DB schema from resources/schema.sql.
     * Uses PreparedStatement (requirement: no Statement usage).
     */
    public static void initSchema() {
        String sql = readResource("schema.sql");
        // Split on semicolons for SQLite batch
        String[] statements = sql.split(";\\s*\\n");

        try (Connection conn = DatabaseConnection.getConnection()) {
            for (String stmtSql : statements) {
                String trimmed = stmtSql.trim();
                if (trimmed.isEmpty()) continue;

                try (PreparedStatement ps = conn.prepareStatement(trimmed)) {
                    ps.execute();
                }
            }
        } catch (SQLException e) {
            throw new DatabaseOperationException("Failed to initialize schema", e);
        }
    }

    private static String readResource(String name) {
        try (InputStream in = DbInit.class.getClassLoader().getResourceAsStream(name)) {
            if (in == null) throw new DatabaseOperationException("Resource not found: " + name);
            return new BufferedReader(new InputStreamReader(in))
                    .lines()
                    .collect(Collectors.joining("\n"));
        } catch (Exception e) {
            throw new DatabaseOperationException("Failed to read resource: " + name, e);
        }
    }
}
