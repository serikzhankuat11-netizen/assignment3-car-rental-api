package model;

public class Truck extends VehicleBase {
    private double cargoCapacityKg;

    public Truck(int id, String name, double pricePerDay, Engine engine, double cargoCapacityKg) {
        super(id, name, pricePerDay, engine);
        this.cargoCapacityKg = cargoCapacityKg;
    }

    @Override
    public double calculateRentalCost(int days) {
        return (days * getPricePerDay()) + 50.0;
    }

    @Override
    public String getType() {
        return "TRUCK";
    }

    public double getCargoCapacityKg() { return cargoCapacityKg; }
    public void setCargoCapacityKg(double cargoCapacityKg) { this.cargoCapacityKg = cargoCapacityKg; }
}
