package aitu.repository;

import aitu.exception.DatabaseOperationException;
import aitu.exception.ResourceNotFoundException;
import aitu.model.Customer;
import aitu.model.Rental;
import aitu.model.VehicleBase;
import aitu.utils.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class RentalRepository {
    private final CustomerRepository customerRepo = new CustomerRepository();
    private final VehicleRepository vehicleRepo = new VehicleRepository();

    public Rental create(Rental r) {
        String sql = "INSERT INTO rentals(customer_id, vehicle_id, days, start_date) VALUES(?,?,?,?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setInt(1, r.getCustomer().getId());
            ps.setInt(2, r.getVehicle().getId());
            ps.setInt(3, r.getDays());
            ps.setString(4, r.getStartDate());
            ps.executeUpdate();

            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) r.setId(rs.getInt(1));
            }
            return r;

        } catch (SQLException e) {
            throw new DatabaseOperationException("Failed to create rental", e);
        }
    }

    public List<Rental> getAll() {
        String sql = "SELECT id, customer_id, vehicle_id, days, start_date FROM rentals ORDER BY id";
        List<Rental> list = new ArrayList<>();

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                list.add(mapRental(rs));
            }
            return list;

        } catch (SQLException e) {
            throw new DatabaseOperationException("Failed to fetch rentals", e);
        }
    }

    public Rental getById(int id) {
        String sql = "SELECT id, customer_id, vehicle_id, days, start_date FROM rentals WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (!rs.next()) throw new ResourceNotFoundException("Rental not found id=" + id);
                return mapRental(rs);
            }

        } catch (SQLException e) {
            throw new DatabaseOperationException("Failed to fetch rental by id", e);
        }
    }

    public void update(int id, int days, String startDate) {
        String sql = "UPDATE rentals SET days=?, start_date=? WHERE id=?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, days);
            ps.setString(2, startDate);
            ps.setInt(3, id);

            int affected = ps.executeUpdate();
            if (affected == 0) throw new ResourceNotFoundException("Rental not found id=" + id);

        } catch (SQLException e) {
            throw new DatabaseOperationException("Failed to update rental", e);
        }
    }

    public void delete(int id) {
        String sql = "DELETE FROM rentals WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);
            int affected = ps.executeUpdate();
            if (affected == 0) throw new ResourceNotFoundException("Rental not found id=" + id);

        } catch (SQLException e) {
            throw new DatabaseOperationException("Failed to delete rental", e);
        }
    }

    private Rental mapRental(ResultSet rs) throws SQLException {
        int id = rs.getInt("id");
        int customerId = rs.getInt("customer_id");
        int vehicleId = rs.getInt("vehicle_id");
        int days = rs.getInt("days");
        String startDate = rs.getString("start_date");

        Customer c = customerRepo.getById(customerId);
        VehicleBase v = vehicleRepo.getById(vehicleId);

        return new Rental(id, c, v, days, startDate);
    }
}
