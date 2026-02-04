package service.interfaces;

import model.VehicleBase;

import java.util.List;

public interface VehicleService {
    void addVehicle(VehicleBase vehicle);
    VehicleBase getById(int id);
    List<VehicleBase> getAllSortedByPrice();
    List<VehicleBase> getCheaperThan(double maxPrice);
    void updateVehicleName(int id, String newName);
    void deleteVehicle(int id);
}
