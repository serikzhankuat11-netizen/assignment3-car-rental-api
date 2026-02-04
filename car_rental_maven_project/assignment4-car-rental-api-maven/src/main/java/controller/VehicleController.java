package controller;

import model.VehicleBase;
import service.interfaces.VehicleService;
import utils.ReflectionUtils;

import java.util.List;
import java.util.Scanner;

public class VehicleController {

    private final VehicleService service;

    public VehicleController(VehicleService service) {
        this.service = service;
    }

    public void runMenu() {
        Scanner sc = new Scanner(System.in);

        while (true) {
            System.out.println("\n--- CAR RENTAL MENU ---");
            System.out.println("1) List vehicles (sorted by price)");
            System.out.println("2) Filter cheaper than");
            System.out.println("3) Reflection inspect first vehicle");
            System.out.println("4) Update vehicle name");
            System.out.println("5) Delete vehicle");
            System.out.println("0) Exit");
            System.out.print("Choose: ");

            String choice = sc.nextLine().trim();
            try {
                switch (choice) {
                    case "1" -> listSorted();
                    case "2" -> filterCheaper(sc);
                    case "3" -> reflectionDemo();
                    case "4" -> updateName(sc);
                    case "5" -> delete(sc);
                    case "0" -> { System.out.println("Bye!"); return; }
                    default -> System.out.println("Wrong choice!");
                }
            } catch (Exception e) {
                System.out.println("ERROR: " + e.getClass().getSimpleName() + " -> " + e.getMessage());
            }
        }
    }

    private void listSorted() {
        List<VehicleBase> list = service.getAllSortedByPrice();
        list.forEach(v -> System.out.println(v.getInfo()));
    }

    private void filterCheaper(Scanner sc) {
        System.out.print("max price: ");
        double max = Double.parseDouble(sc.nextLine().trim());
        service.getCheaperThan(max).forEach(v -> System.out.println(v.getInfo()));
    }

    private void reflectionDemo() {
        List<VehicleBase> list = service.getAllSortedByPrice();
        if (list.isEmpty()) {
            System.out.println("No vehicles.");
            return;
        }
        ReflectionUtils.inspect(list.get(0));
    }

    private void updateName(Scanner sc) {
        System.out.print("id: ");
        int id = Integer.parseInt(sc.nextLine().trim());
        System.out.print("new name: ");
        String name = sc.nextLine();
        service.updateVehicleName(id, name);
        System.out.println("Updated.");
    }

    private void delete(Scanner sc) {
        System.out.print("id: ");
        int id = Integer.parseInt(sc.nextLine().trim());
        service.deleteVehicle(id);
        System.out.println("Deleted.");
    }
}
