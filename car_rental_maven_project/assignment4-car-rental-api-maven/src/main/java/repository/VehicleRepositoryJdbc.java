package repository;

import exception.DatabaseOperationException;
import exception.ResourceNotFoundException;
import model.Car;
import model.Engine;
import model.Truck;
import model.VehicleBase;
import repository.interfaces.CrudRepository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class VehicleRepositoryJdbc implements CrudRepository<VehicleBase> {

    private final Connection connection;

    public VehicleRepositoryJdbc(Connection connection) {
        this.connection = connection;
    }

    @Override
    public void create(VehicleBase vehicle) {
        String sql = "INSERT INTO vehicles(name, price_per_day, type) VALUES (?, ?, ?) RETURNING id";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, vehicle.getName());
            ps.setDouble(2, vehicle.getPricePerDay());
            ps.setString(3, vehicle.getType());

            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                int id = rs.getInt("id");
                vehicle.setId(id);

                if (vehicle.getEngine() != null) {
                    insertEngine(id, vehicle.getEngine());
                }

                if (vehicle instanceof Car car) {
                    insertCarDetails(id, car.getSeats());
                } else if (vehicle instanceof Truck truck) {
                    insertTruckDetails(id, truck.getCargoCapacityKg());
                }
            }
        } catch (SQLException e) {
            throw new DatabaseOperationException("Create vehicle failed", e);
        }
    }

    private void insertEngine(int vehicleId, Engine engine) throws SQLException {
        String sql = "INSERT INTO engines(vehicle_id, model, horsepower) VALUES (?, ?, ?)";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, vehicleId);
            ps.setString(2, engine.getModel());
            ps.setInt(3, engine.getHorsepower());
            ps.executeUpdate();
        }
    }

    private void insertCarDetails(int vehicleId, int seats) throws SQLException {
        String sql = "INSERT INTO car_details(vehicle_id, seats) VALUES (?, ?)";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, vehicleId);
            ps.setInt(2, seats);
            ps.executeUpdate();
        }
    }

    private void insertTruckDetails(int vehicleId, double cargoCapacityKg) throws SQLException {
        String sql = "INSERT INTO truck_details(vehicle_id, cargo_capacity_kg) VALUES (?, ?)";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, vehicleId);
            ps.setDouble(2, cargoCapacityKg);
            ps.executeUpdate();
        }
    }

    @Override
    public VehicleBase findById(int id) {
        String sql =
                "SELECT v.id, v.name, v.price_per_day, v.type, " +
                "e.model AS engine_model, e.horsepower AS engine_hp, " +
                "cd.seats, td.cargo_capacity_kg " +
                "FROM vehicles v " +
                "LEFT JOIN engines e ON e.vehicle_id = v.id " +
                "LEFT JOIN car_details cd ON cd.vehicle_id = v.id " +
                "LEFT JOIN truck_details td ON td.vehicle_id = v.id " +
                "WHERE v.id = ?";

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (!rs.next()) throw new ResourceNotFoundException("Vehicle not found: id=" + id);
            return mapRowToVehicle(rs);
        } catch (SQLException e) {
            throw new DatabaseOperationException("FindById failed", e);
        }
    }

    @Override
    public List<VehicleBase> findAll() {
        String sql =
                "SELECT v.id, v.name, v.price_per_day, v.type, " +
                "e.model AS engine_model, e.horsepower AS engine_hp, " +
                "cd.seats, td.cargo_capacity_kg " +
                "FROM vehicles v " +
                "LEFT JOIN engines e ON e.vehicle_id = v.id " +
                "LEFT JOIN car_details cd ON cd.vehicle_id = v.id " +
                "LEFT JOIN truck_details td ON td.vehicle_id = v.id " +
                "ORDER BY v.id";

        List<VehicleBase> list = new ArrayList<>();
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                list.add(mapRowToVehicle(rs));
            }
        } catch (SQLException e) {
            throw new DatabaseOperationException("FindAll failed", e);
        }
        return list;
    }

    @Override
    public void update(VehicleBase vehicle) {
        String sql = "UPDATE vehicles SET name=?, price_per_day=? WHERE id=?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, vehicle.getName());
            ps.setDouble(2, vehicle.getPricePerDay());
            ps.setInt(3, vehicle.getId());

            int rows = ps.executeUpdate();
            if (rows == 0) throw new ResourceNotFoundException("Vehicle not found: id=" + vehicle.getId());
        } catch (SQLException e) {
            throw new DatabaseOperationException("Update failed", e);
        }
    }

    @Override
    public void delete(int id) {
        try {
            deleteChildTables(id);

            String sql = "DELETE FROM vehicles WHERE id=?";
            try (PreparedStatement ps = connection.prepareStatement(sql)) {
                ps.setInt(1, id);
                int rows = ps.executeUpdate();
                if (rows == 0) throw new ResourceNotFoundException("Vehicle not found: id=" + id);
            }
        } catch (SQLException e) {
            throw new DatabaseOperationException("Delete failed", e);
        }
    }

    private void deleteChildTables(int vehicleId) throws SQLException {
        try (PreparedStatement ps1 = connection.prepareStatement("DELETE FROM engines WHERE vehicle_id=?")) {
            ps1.setInt(1, vehicleId);
            ps1.executeUpdate();
        }
        try (PreparedStatement ps2 = connection.prepareStatement("DELETE FROM car_details WHERE vehicle_id=?")) {
            ps2.setInt(1, vehicleId);
            ps2.executeUpdate();
        }
        try (PreparedStatement ps3 = connection.prepareStatement("DELETE FROM truck_details WHERE vehicle_id=?")) {
            ps3.setInt(1, vehicleId);
            ps3.executeUpdate();
        }
    }

    private VehicleBase mapRowToVehicle(ResultSet rs) throws SQLException {
        int id = rs.getInt("id");
        String name = rs.getString("name");
        double price = rs.getDouble("price_per_day");
        String type = rs.getString("type");

        String engineModel = rs.getString("engine_model");
        int engineHp = rs.getInt("engine_hp");
        Engine engine = (engineModel == null) ? null : new Engine(engineModel, engineHp);

        if ("CAR".equalsIgnoreCase(type)) {
            int seats = rs.getInt("seats");
            return new Car(id, name, price, engine, seats);
        } else if ("TRUCK".equalsIgnoreCase(type)) {
            double cap = rs.getDouble("cargo_capacity_kg");
            return new Truck(id, name, price, engine, cap);
        } else {
            return new Car(id, name, price, engine, 4);
        }
    }
}
