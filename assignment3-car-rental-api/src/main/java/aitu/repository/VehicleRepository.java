package aitu.repository;

import aitu.exception.DatabaseOperationException;
import aitu.exception.ResourceNotFoundException;
import aitu.model.*;

import aitu.utils.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class VehicleRepository {

    public VehicleBase create(VehicleBase v) {
        String sql = "INSERT INTO vehicles(name, price_per_day, type, seats, capacity_tons) VALUES(?,?,?,?,?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, v.getName());
            ps.setDouble(2, v.getPricePerDay());
            ps.setString(3, v.getType().name());

            if (v instanceof Car car) {
                ps.setInt(4, car.getSeats());
                ps.setNull(5, Types.REAL);
            } else if (v instanceof Truck truck) {
                ps.setNull(4, Types.INTEGER);
                ps.setDouble(5, truck.getCapacityTons());
            } else {
                ps.setNull(4, Types.INTEGER);
                ps.setNull(5, Types.REAL);
            }

            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) v.setId(rs.getInt(1));
            }
            return v;

        } catch (SQLException e) {
            throw new DatabaseOperationException("Failed to create vehicle", e);
        }
    }

    public List<VehicleBase> getAll() {
        String sql = "SELECT id, name, price_per_day, type, seats, capacity_tons FROM vehicles ORDER BY id";
        List<VehicleBase> list = new ArrayList<>();

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                list.add(mapVehicle(rs));
            }
            return list;

        } catch (SQLException e) {
            throw new DatabaseOperationException("Failed to fetch vehicles", e);
        }
    }

    public VehicleBase getById(int id) {
        String sql = "SELECT id, name, price_per_day, type, seats, capacity_tons FROM vehicles WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (!rs.next()) throw new ResourceNotFoundException("Vehicle not found id=" + id);
                return mapVehicle(rs);
            }

        } catch (SQLException e) {
            throw new DatabaseOperationException("Failed to fetch vehicle by id", e);
        }
    }

    public void update(int id, VehicleBase v) {
        String sql = "UPDATE vehicles SET name=?, price_per_day=?, type=?, seats=?, capacity_tons=? WHERE id=?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, v.getName());
            ps.setDouble(2, v.getPricePerDay());
            ps.setString(3, v.getType().name());

            if (v instanceof Car car) {
                ps.setInt(4, car.getSeats());
                ps.setNull(5, Types.REAL);
            } else if (v instanceof Truck truck) {
                ps.setNull(4, Types.INTEGER);
                ps.setDouble(5, truck.getCapacityTons());
            } else {
                ps.setNull(4, Types.INTEGER);
                ps.setNull(5, Types.REAL);
            }

            ps.setInt(6, id);

            int affected = ps.executeUpdate();
            if (affected == 0) throw new ResourceNotFoundException("Vehicle not found id=" + id);

        } catch (SQLException e) {
            throw new DatabaseOperationException("Failed to update vehicle", e);
        }
    }

    public void delete(int id) {
        String sql = "DELETE FROM vehicles WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);
            int affected = ps.executeUpdate();
            if (affected == 0) throw new ResourceNotFoundException("Vehicle not found id=" + id);

        } catch (SQLException e) {
            throw new DatabaseOperationException("Failed to delete vehicle (maybe referenced by rentals)", e);
        }
    }

    private VehicleBase mapVehicle(ResultSet rs) throws SQLException {
        int id = rs.getInt("id");
        String name = rs.getString("name");
        double price = rs.getDouble("price_per_day");
        VehicleType type = VehicleType.valueOf(rs.getString("type"));

        if (type == VehicleType.CAR) {
            int seats = rs.getInt("seats");
            return new Car(id, name, price, seats);
        } else {
            double cap = rs.getDouble("capacity_tons");
            return new Truck(id, name, price, cap);
        }
    }
}
