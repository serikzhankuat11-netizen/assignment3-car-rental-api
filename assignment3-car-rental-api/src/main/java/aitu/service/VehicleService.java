package aitu.service;

import aitu.model.VehicleBase;
import aitu.repository.VehicleRepository;

import java.util.List;

public class VehicleService {
    private final VehicleRepository repo = new VehicleRepository();

    public VehicleBase create(VehicleBase v) {
        v.validate();
        return repo.create(v);
    }

    public List<VehicleBase> getAll() { return repo.getAll(); }

    public VehicleBase getById(int id) { return repo.getById(id); }

    public void update(int id, VehicleBase updated) {
        updated.validate();
        repo.update(id, updated);
    }

    public void delete(int id) { repo.delete(id); }
}
