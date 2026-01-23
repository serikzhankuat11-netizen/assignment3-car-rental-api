package aitu.model;

import aitu.exception.InvalidInputException;

public class Truck extends VehicleBase {
    private double capacityTons;

    public Truck(int id, String name, double pricePerDay, double capacityTons) {
        super(id, name, pricePerDay);
        this.capacityTons = capacityTons;
    }

    @Override
    public VehicleType getType() {
        return VehicleType.TRUCK;
    }

    public double getCapacityTons() { return capacityTons; }

    public void setCapacityTons(double capacityTons) {
        if (capacityTons <= 0) throw new InvalidInputException("Capacity must be > 0 tons");
        this.capacityTons = capacityTons;
    }

    /**
     * Business rule: trucks have +20% daily fee.
     */
    @Override
    public double calculateRent(int days) {
        if (days <= 0) throw new InvalidInputException("Days must be > 0");
        return pricePerDay * days * 1.20;
    }

    @Override
    public String describe() {
        return "Truck{id=" + id + ", name='" + name + "', pricePerDay=" + pricePerDay + ", capacityTons=" + capacityTons + "}";
    }

    @Override
    public void validate() {
        super.validate();
        if (capacityTons <= 0) throw new InvalidInputException("Truck capacity must be > 0 tons");
    }
}
