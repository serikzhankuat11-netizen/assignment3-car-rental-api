package aitu.model;

import aitu.exception.InvalidInputException;

public class Rental {
    private int id;
    private Customer customer;     // composition
    private VehicleBase vehicle;   // composition
    private int days;
    private String startDate;      // ISO: yyyy-MM-dd

    public Rental(int id, Customer customer, VehicleBase vehicle, int days, String startDate) {
        this.id = id;
        this.customer = customer;
        this.vehicle = vehicle;
        this.days = days;
        this.startDate = startDate;
    }

    public double totalPrice() {
        return vehicle.calculateRent(days);
    }

    public void validate() {
        if (customer == null) throw new InvalidInputException("Customer is required");
        if (vehicle == null) throw new InvalidInputException("Vehicle is required");
        if (days <= 0) throw new InvalidInputException("Days must be > 0");
        if (startDate == null || startDate.trim().isEmpty()) throw new InvalidInputException("Start date is required");
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public Customer getCustomer() { return customer; }
    public VehicleBase getVehicle() { return vehicle; }
    public int getDays() { return days; }
    public String getStartDate() { return startDate; }

    public void setDays(int days) {
        if (days <= 0) throw new InvalidInputException("Days must be > 0");
        this.days = days;
    }

    @Override
    public String toString() {
        return "Rental{id=" + id +
                ", customer=" + customer.displayName() +
                ", vehicle=" + vehicle.displayName() + " (" + vehicle.getType() + ")" +
                ", days=" + days +
                ", startDate='" + startDate + '\'' +
                ", total=" + totalPrice() +
                '}';
    }
}
