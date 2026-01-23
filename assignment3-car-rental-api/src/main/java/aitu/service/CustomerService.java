package aitu.service;

import aitu.model.Customer;
import aitu.repository.CustomerRepository;

import java.util.List;

public class CustomerService {
    private final CustomerRepository repo = new CustomerRepository();

    public Customer create(Customer c) {
        c.validate();
        return repo.create(c);
    }

    public List<Customer> getAll() { return repo.getAll(); }

    public Customer getById(int id) { return repo.getById(id); }

    public void update(int id, Customer updated) {
        updated.validate();
        repo.update(id, updated);
    }

    public void delete(int id) { repo.delete(id); }
}
