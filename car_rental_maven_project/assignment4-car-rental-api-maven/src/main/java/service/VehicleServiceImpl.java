package service;

import exception.DuplicateResourceException;
import exception.InvalidInputException;
import model.VehicleBase;
import repository.interfaces.CrudRepository;
import service.interfaces.VehicleService;
import utils.SortingUtils;

import java.util.List;

public class VehicleServiceImpl implements VehicleService {

    private final CrudRepository<VehicleBase> repo;

    public VehicleServiceImpl(CrudRepository<VehicleBase> repo) {
        this.repo = repo;
    }

    @Override
    public void addVehicle(VehicleBase vehicle) {
        validateVehicle(vehicle);

        boolean exists = repo.findAll().stream()
                .anyMatch(v -> v.getName().equalsIgnoreCase(vehicle.getName()));
        if (exists) throw new DuplicateResourceException("Vehicle already exists with name: " + vehicle.getName());

        repo.create(vehicle);
    }

    @Override
    public VehicleBase getById(int id) {
        if (id <= 0) throw new InvalidInputException("id must be positive");
        return repo.findById(id);
    }

    @Override
    public List<VehicleBase> getAllSortedByPrice() {
        List<VehicleBase> list = repo.findAll();
        SortingUtils.sortByPricePerDayAsc(list);
        return list;
    }

    @Override
    public List<VehicleBase> getCheaperThan(double maxPrice) {
        if (maxPrice <= 0) throw new InvalidInputException("maxPrice must be positive");
        return SortingUtils.filterCheaperThan(repo.findAll(), maxPrice);
    }

    @Override
    public void updateVehicleName(int id, String newName) {
        if (id <= 0) throw new InvalidInputException("id must be positive");
        if (newName == null || newName.isBlank()) throw new InvalidInputException("newName cannot be empty");
        VehicleBase v = repo.findById(id);
        v.setName(newName.trim());
        repo.update(v);
    }

    @Override
    public void deleteVehicle(int id) {
        if (id <= 0) throw new InvalidInputException("id must be positive");
        repo.delete(id);
    }

    private void validateVehicle(VehicleBase v) {
        if (v == null) throw new InvalidInputException("vehicle is null");
        if (v.getName() == null || v.getName().isBlank()) throw new InvalidInputException("name cannot be empty");
        if (v.getPricePerDay() <= 0) throw new InvalidInputException("pricePerDay must be positive");
        if (v.getEngine() != null && v.getEngine().getHorsepower() <= 0)
            throw new InvalidInputException("engine horsepower must be positive");
    }
}
