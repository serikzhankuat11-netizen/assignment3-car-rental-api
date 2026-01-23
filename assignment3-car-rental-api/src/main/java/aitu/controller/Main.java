package aitu.controller;

import aitu.exception.DatabaseOperationException;
import aitu.exception.DuplicateResourceException;
import aitu.exception.InvalidInputException;
import aitu.exception.ResourceNotFoundException;
import aitu.model.*;
import aitu.service.CustomerService;
import aitu.service.RentalService;
import aitu.service.VehicleService;
import aitu.utils.DbInit;

import java.util.List;
import java.util.Scanner;

public class Main {

    private static final CustomerService customerService = new CustomerService();
    private static final VehicleService vehicleService = new VehicleService();
    private static final RentalService rentalService = new RentalService();

    public static void main(String[] args) {
        // Init DB + sample data
        DbInit.initSchema();

        System.out.println("=== Car Rental API (CLI demo) ===");
        System.out.println("DB initialized. (SQLite file: data/car_rental.db)");
        System.out.println();

        seedIfEmpty();

        try (Scanner sc = new Scanner(System.in)) {
            while (true) {
                printMenu();
                String choice = sc.nextLine().trim();

                try {
                    switch (choice) {
                        case "1" -> listCustomers();
                        case "2" -> addCustomer(sc);
                        case "3" -> updateCustomer(sc);
                        case "4" -> deleteCustomer(sc);

                        case "5" -> listVehiclesPolymorphismDemo();
                        case "6" -> addVehicle(sc);
                        case "7" -> updateVehicle(sc);
                        case "8" -> deleteVehicle(sc);

                        case "9" -> listRentals();
                        case "10" -> addRental(sc);
                        case "11" -> updateRental(sc);
                        case "12" -> deleteRental(sc);

                        case "0" -> {
                            System.out.println("Bye!");
                            return;
                        }
                        default -> System.out.println("Unknown option.");
                    }
                } catch (DuplicateResourceException e) {
                System.out.println("❌ ERROR: " + e.getMessage());
            } catch (InvalidInputException e) {
                System.out.println("❌ ERROR: " + e.getMessage());
            } catch (ResourceNotFoundException e) {
                System.out.println("❌ ERROR: " + e.getMessage());
            } catch (DatabaseOperationException e) {
                System.out.println("❌ DB ERROR: " + e.getMessage());
            } catch (Exception e) {
                System.out.println("❌ Unexpected: " + e.getMessage());
            }


            System.out.println();
            }
        }
    }

    private static void printMenu() {
        System.out.println("----- MENU -----");
        System.out.println("Customers:");
        System.out.println("  1) List customers");
        System.out.println("  2) Add customer");
        System.out.println("  3) Update customer");
        System.out.println("  4) Delete customer");
        System.out.println("Vehicles:");
        System.out.println("  5) List vehicles (polymorphism demo)");
        System.out.println("  6) Add vehicle (car/truck)");
        System.out.println("  7) Update vehicle");
        System.out.println("  8) Delete vehicle");
        System.out.println("Rentals:");
        System.out.println("  9) List rentals");
        System.out.println(" 10) Add rental");
        System.out.println(" 11) Update rental");
        System.out.println(" 12) Delete rental");
        System.out.println("Other:");
        System.out.println("  0) Exit");
        System.out.print("Choose: ");
    }

    // ---------- Customers ----------
    private static void listCustomers() {
        List<Customer> customers = customerService.getAll();
        System.out.println("Customers (" + customers.size() + "):");
        for (Customer c : customers) {
            System.out.println(" - " + c.describe());
        }
    }

    private static void addCustomer(Scanner sc) {
        System.out.print("Full name: ");
        String name = sc.nextLine();
        Customer c = new Customer(0, name);
        customerService.create(c);
        System.out.println("✅ Created: " + c.describe());
    }

    private static void updateCustomer(Scanner sc) {
        int id = readInt(sc, "Customer id: ");
        System.out.print("New full name: ");
        String name = sc.nextLine();
        customerService.update(id, new Customer(id, name));
        System.out.println("✅ Updated customer id=" + id);
    }

    private static void deleteCustomer(Scanner sc) {
        int id = readInt(sc, "Customer id: ");
        customerService.delete(id);
        System.out.println("✅ Deleted customer id=" + id);
    }

    // ---------- Vehicles ----------
    private static void listVehiclesPolymorphismDemo() {
        List<VehicleBase> vehicles = vehicleService.getAll();
        System.out.println("Vehicles (" + vehicles.size() + "):");
        int demoDays = 3;

        // Polymorphism: calling overridden methods via base reference
        for (VehicleBase v : vehicles) {
            System.out.println(" - " + v.describe()
                    + " | type=" + v.getType()
                    + " | price/day=" + v.getPricePerDay()
                    + " | rent(" + demoDays + " days)=" + v.calculateRent(demoDays));
        }

        // Interface call demo
        if (!vehicles.isEmpty()) {
            PricedItem p = vehicles.get(0);
            System.out.println("Interface demo (PricedItem.getPricePerDay): " + p.getPricePerDay());
        }
    }

    private static void addVehicle(Scanner sc) {
        System.out.print("Type (1=Car, 2=Truck): ");
        String t = sc.nextLine().trim();
        System.out.print("Name: ");
        String name = sc.nextLine();
        double price = readDouble(sc, "Price per day: ");

        VehicleBase v;
        if (t.equals("1")) {
            int seats = readInt(sc, "Seats: ");
            v = new Car(0, name, price, seats);
        } else if (t.equals("2")) {
            double cap = readDouble(sc, "Capacity (tons): ");
            v = new Truck(0, name, price, cap);
        } else {
            throw new InvalidInputException("Type must be 1 or 2");
        }

        vehicleService.create(v);
        System.out.println("✅ Created: " + v.describe());
    }

    private static void updateVehicle(Scanner sc) {
        int id = readInt(sc, "Vehicle id: ");
        VehicleBase existing = vehicleService.getById(id);

        System.out.println("Existing: " + existing.describe());
        System.out.print("New name: ");
        String name = sc.nextLine();
        double price = readDouble(sc, "New price per day: ");

        VehicleBase updated;
        if (existing instanceof Car) {
            int seats = readInt(sc, "New seats: ");
            updated = new Car(id, name, price, seats);
        } else {
            double cap = readDouble(sc, "New capacity (tons): ");
            updated = new Truck(id, name, price, cap);
        }

        vehicleService.update(id, updated);
        System.out.println("✅ Updated vehicle id=" + id);
    }

    private static void deleteVehicle(Scanner sc) {
        int id = readInt(sc, "Vehicle id: ");
        vehicleService.delete(id);
        System.out.println("✅ Deleted vehicle id=" + id);
    }

    // ---------- Rentals ----------
    private static void listRentals() {
        List<Rental> rentals = rentalService.getAll();
        System.out.println("Rentals (" + rentals.size() + "):");
        for (Rental r : rentals) {
            System.out.println(" - " + r);
        }
    }

    private static void addRental(Scanner sc) {
        int customerId = readInt(sc, "Customer id: ");
        int vehicleId = readInt(sc, "Vehicle id: ");
        int days = readInt(sc, "Days: ");
        System.out.print("Start date (yyyy-MM-dd): ");
        String start = sc.nextLine().trim();

        Rental r = rentalService.create(customerId, vehicleId, days, start);
        System.out.println("✅ Created rental: " + r);
    }

    private static void updateRental(Scanner sc) {
        int id = readInt(sc, "Rental id: ");
        int days = readInt(sc, "New days: ");
        System.out.print("New start date (yyyy-MM-dd): ");
        String start = sc.nextLine().trim();
        rentalService.update(id, days, start);
        System.out.println("✅ Updated rental id=" + id);
    }

    private static void deleteRental(Scanner sc) {
        int id = readInt(sc, "Rental id: ");
        rentalService.delete(id);
        System.out.println("✅ Deleted rental id=" + id);
    }

    // ---------- Helpers ----------
    private static int readInt(Scanner sc, String prompt) {
        System.out.print(prompt);
        String s = sc.nextLine().trim();
        try { return Integer.parseInt(s); }
        catch (Exception e) { throw new InvalidInputException("Must be an integer"); }
    }

    private static double readDouble(Scanner sc, String prompt) {
        System.out.print(prompt);
        String s = sc.nextLine().trim();
        try { return Double.parseDouble(s); }
        catch (Exception e) { throw new InvalidInputException("Must be a number"); }
    }

    private static void seedIfEmpty() {
        // Create extra demo data only if empty
        if (customerService.getAll().isEmpty()) {
            customerService.create(new Customer(0, "Ali Nur"));
            customerService.create(new Customer(0, "Zhankuat Serik"));
        }
        if (vehicleService.getAll().isEmpty()) {
            vehicleService.create(new Car(0, "Toyota Camry", 15000, 5));
            vehicleService.create(new Truck(0, "Volvo FH", 30000, 15.0));
        }
    }
}
