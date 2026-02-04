package model;

public class Car extends VehicleBase {
    private int seats;

    public Car(int id, String name, double pricePerDay, Engine engine, int seats) {
        super(id, name, pricePerDay, engine);
        this.seats = seats;
    }

    @Override
    public double calculateRentalCost(int days) {
        return days * getPricePerDay();
    }

    @Override
    public String getType() {
        return "CAR";
    }

    public int getSeats() { return seats; }
    public void setSeats(int seats) { this.seats = seats; }
}
