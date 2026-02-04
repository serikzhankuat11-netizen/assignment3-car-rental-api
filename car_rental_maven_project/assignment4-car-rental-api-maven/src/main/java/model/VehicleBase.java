package model;

public abstract class VehicleBase {
    private int id;
    private String name;
    private double pricePerDay;

    // Composition
    private Engine engine;

    protected VehicleBase(int id, String name, double pricePerDay, Engine engine) {
        this.id = id;
        this.name = name;
        this.pricePerDay = pricePerDay;
        this.engine = engine;
    }

    public abstract double calculateRentalCost(int days);
    public abstract String getType();

    public String getInfo() {
        return "#" + id + " " + name + " | type=" + getType()
                + " | pricePerDay=" + pricePerDay
                + " | engine=" + (engine == null ? "none" : engine.toString());
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public double getPricePerDay() { return pricePerDay; }
    public void setPricePerDay(double pricePerDay) { this.pricePerDay = pricePerDay; }

    public Engine getEngine() { return engine; }
    public void setEngine(Engine engine) { this.engine = engine; }
}
