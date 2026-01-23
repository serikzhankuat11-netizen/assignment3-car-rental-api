# Assignment 3 — Advanced OOP API Project (JDBC + Exception Handling)
Topic: **Car Rental API (CLI demo)**

This project satisfies the requirements from the assignment PDF:
- Multi-layer architecture: **controller → service → repository**
- JDBC CRUD via **PreparedStatement** (no Statement)
- OOP: **Encapsulation, Inheritance, Abstract classes, Interfaces, Polymorphism, Composition**
- Custom exception hierarchy
- Validation & business rules in service layer
- Relational DB with foreign keys (SQLite)

## 1) Entities & Relationships
- **Customer** (customers table)
- **VehicleBase (abstract)** → subclasses: **Car**, **Truck** (vehicles table)
- **Rental** has-a **Customer + VehicleBase** (composition) (rentals table)
- rentals.customer_id → customers.id (FK)
- rentals.vehicle_id → vehicles.id (FK)

## 2) OOP Design
### Abstract class
- `BaseEntity` (id, name) has:
  - 2 abstract methods: `describe()`, `validate()`
  - 1 concrete method: `displayName()`

### Inheritance
- `Customer extends BaseEntity`
- `VehicleBase extends BaseEntity`
  - `Car extends VehicleBase`
  - `Truck extends VehicleBase`

### Interfaces
- `Validatable`: `void validate()`
- `PricedItem`: `double getPricePerDay()`

### Polymorphism example
Vehicles are stored and used as `VehicleBase` references, calling overridden methods:
- `getType()`
- `calculateRent(days)`
- `describe()`

### Composition
`Rental` contains a `Customer` and a `VehicleBase` object.

## 3) Database
Schema: `src/main/resources/schema.sql`  
SQLite DB file is created automatically in `data/car_rental.db`.

## 4) How to Run
### Option A — IntelliJ IDEA
1. Open IntelliJ → **File → Open** → select this folder.
2. Make sure JDK 17 is set (Project Structure).
3. Maven will import dependencies.
4. Run: `aitu.controller.Main`

### Option B — Terminal
```bash
mvn -q clean package
java -jar target/assignment3-car-rental-api-1.0.0.jar
```

`

## 6) Reflection (fill in)
- What I learned:
- Challenges:
- Benefits of JDBC + layered design:
