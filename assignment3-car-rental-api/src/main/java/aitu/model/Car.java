package aitu.model;

import aitu.exception.InvalidInputException;

public class Car extends VehicleBase {
    private int seats;

    public Car(int id, String name, double pricePerDay, int seats) {
        super(id, name, pricePerDay);
        this.seats = seats;
    }

    @Override
    public VehicleType getType() {
        return VehicleType.CAR;
    }

    public int getSeats() { return seats; }

    public void setSeats(int seats) {
        if (seats <= 0) throw new InvalidInputException("Seats must be > 0");
        this.seats = seats;
    }

    @Override
    public String describe() {
        return "Car{id=" + id + ", name='" + name + "', pricePerDay=" + pricePerDay + ", seats=" + seats + "}";
    }

    @Override
    public void validate() {
        super.validate();
        if (seats <= 0) throw new InvalidInputException("Car seats must be > 0");
    }
}
