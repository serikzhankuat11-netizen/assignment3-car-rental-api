import controller.VehicleController;
import model.Car;
import model.Engine;
import model.Truck;
import repository.VehicleRepositoryJdbc;
import service.VehicleServiceImpl;
import service.interfaces.VehicleService;

import java.sql.Connection;

public class Main {
    public static void main(String[] args) {

        try (Connection conn = DatabaseConnection.getConnection()) {

            VehicleRepositoryJdbc repo = new VehicleRepositoryJdbc(conn);
            VehicleService service = new VehicleServiceImpl(repo);

            // demo data (create)
            service.addVehicle(new Car(0, "Toyota Camry", 80, new Engine("2AR-FE", 178), 5));
            service.addVehicle(new Truck(0, "Ford F-150", 120, new Engine("V8", 400), 1000));

            // show simple output + reflection example
            System.out.println("=== Sorted vehicles by price ===");
            service.getAllSortedByPrice().forEach(v -> System.out.println(v.getInfo()));

            // optional CLI menu (uncomment if you want interactive)
            // VehicleController controller = new VehicleController(service);
            // controller.runMenu();

        } catch (Exception e) {
            System.out.println("FATAL: " + e.getClass().getSimpleName() + " -> " + e.getMessage());
        }
    }
}
