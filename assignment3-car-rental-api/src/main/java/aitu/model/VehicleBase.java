package aitu.model;

import aitu.exception.InvalidInputException;

public abstract class VehicleBase extends BaseEntity implements PricedItem, Validatable {

    protected double pricePerDay;

    protected VehicleBase(int id, String name, double pricePerDay) {
        super(id, name);
        this.pricePerDay = pricePerDay;
    }

    public abstract VehicleType getType();

    /**
     * Polymorphism: subclasses can customize rent calculation.
     */
    public double calculateRent(int days) {
        if (days <= 0) throw new InvalidInputException("Days must be > 0");
        return pricePerDay * days;
    }

    @Override
    public double getPricePerDay() {
        return pricePerDay;
    }

    public void setPricePerDay(double pricePerDay) {
        if (pricePerDay <= 0) throw new InvalidInputException("Price per day must be > 0");
        this.pricePerDay = pricePerDay;
    }

    @Override
    public void validate() {
        if (name == null || name.trim().isEmpty()) throw new InvalidInputException("Vehicle name must not be empty");
        if (pricePerDay <= 0) throw new InvalidInputException("Vehicle price per day must be > 0");
    }
}
