package aitu.repository;

import aitu.exception.DatabaseOperationException;
import aitu.exception.DuplicateResourceException;
import aitu.exception.ResourceNotFoundException;
import aitu.model.Customer;
import aitu.utils.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CustomerRepository {

    public Customer create(Customer c) {
        String sql = "INSERT INTO customers(full_name) VALUES (?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, c.getFullName());
            ps.executeUpdate();

            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) c.setId(rs.getInt(1));
            }
            return c;

        } catch (SQLException e) {
            if (e.getMessage() != null && e.getMessage().toLowerCase().contains("unique")) {
                throw new DuplicateResourceException("Customer already exists: " + c.getFullName());
            }
            throw new DatabaseOperationException("Failed to create customer", e);
        }
    }

    public List<Customer> getAll() {
        String sql = "SELECT id, full_name FROM customers ORDER BY id";
        List<Customer> list = new ArrayList<>();
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                list.add(new Customer(rs.getInt("id"), rs.getString("full_name")));
            }
            return list;

        } catch (SQLException e) {
            throw new DatabaseOperationException("Failed to fetch customers", e);
        }
    }

    public Customer getById(int id) {
        String sql = "SELECT id, full_name FROM customers WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (!rs.next()) throw new ResourceNotFoundException("Customer not found id=" + id);
                return new Customer(rs.getInt("id"), rs.getString("full_name"));
            }

        } catch (SQLException e) {
            throw new DatabaseOperationException("Failed to fetch customer by id", e);
        }
    }

    public void update(int id, Customer updated) {
        String sql = "UPDATE customers SET full_name = ? WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, updated.getFullName());
            ps.setInt(2, id);
            int affected = ps.executeUpdate();
            if (affected == 0) throw new ResourceNotFoundException("Customer not found id=" + id);

        } catch (SQLException e) {
            if (e.getMessage() != null && e.getMessage().toLowerCase().contains("unique")) {
                throw new DuplicateResourceException("Customer already exists: " + updated.getFullName());
            }
            throw new DatabaseOperationException("Failed to update customer", e);
        }
    }

    public void delete(int id) {
        String sql = "DELETE FROM customers WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);
            int affected = ps.executeUpdate();
            if (affected == 0) throw new ResourceNotFoundException("Customer not found id=" + id);

        } catch (SQLException e) {
            // FK restriction errors will come here
            throw new DatabaseOperationException("Failed to delete customer (maybe referenced by rentals)", e);
        }
    }
}
