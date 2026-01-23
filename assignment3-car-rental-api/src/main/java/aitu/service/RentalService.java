package aitu.service;

import aitu.exception.InvalidInputException;
import aitu.model.Customer;
import aitu.model.Rental;
import aitu.model.VehicleBase;
import aitu.repository.RentalRepository;

import java.time.LocalDate;
import java.util.List;

public class RentalService {
    private final RentalRepository repo = new RentalRepository();
    private final CustomerService customerService = new CustomerService();
    private final VehicleService vehicleService = new VehicleService();

    /**
     * Business rules:
     * - customer must exist
     * - vehicle must exist
     * - days > 0
     * - startDate must be valid ISO yyyy-MM-dd
     */
    public Rental create(int customerId, int vehicleId, int days, String startDate) {
        if (days <= 0) throw new InvalidInputException("Days must be > 0");
        validateDate(startDate);

        Customer c = customerService.getById(customerId);
        VehicleBase v = vehicleService.getById(vehicleId);

        Rental r = new Rental(0, c, v, days, startDate);
        r.validate();
        return repo.create(r);
    }

    public List<Rental> getAll() { return repo.getAll(); }
    public Rental getById(int id) { return repo.getById(id); }

    public void update(int id, int days, String startDate) {
        if (days <= 0) throw new InvalidInputException("Days must be > 0");
        validateDate(startDate);
        repo.update(id, days, startDate);
    }

    public void delete(int id) { repo.delete(id); }

    private void validateDate(String iso) {
        try {
            LocalDate.parse(iso);
        } catch (Exception e) {
            throw new InvalidInputException("Start date must be ISO format yyyy-MM-dd");
        }
    }
}
