package aitu.model;

import aitu.exception.InvalidInputException;

public class Customer extends BaseEntity implements Validatable {

    public Customer(int id, String fullName) {
        super(id, fullName);
    }

    public String getFullName() {
        return name;
    }

    public void setFullName(String fullName) {
        setName(fullName);
    }

    @Override
    public String describe() {
        return "Customer{id=" + id + ", fullName='" + name + "'}";
    }

    @Override
    public void validate() {
        if (name == null || name.trim().isEmpty()) {
            throw new InvalidInputException("Customer full name must not be empty");
        }
    }
}
