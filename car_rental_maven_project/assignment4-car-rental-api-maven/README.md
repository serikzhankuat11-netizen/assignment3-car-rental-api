# Assignment 4 — SOLID Architecture & Advanced OOP (Car Rental API)

## 1) Project Overview
This project is a refactored **Car Rental** system built using a strict layered architecture:

**controller → service → repository → database (PostgreSQL)**

The goal is to demonstrate **SOLID principles** in a real Java project and use **advanced OOP features** such as:
- Abstract classes + polymorphism
- Interfaces (ISP) + default/static methods usage concept
- Generics (Generic CRUD repository)
- Lambdas (sorting/filtering)
- Reflection / RTTI (runtime inspection utility)
- Exception hierarchy applied in the **service layer**

---

## 2) Domain Model (Car Rental)
### Core abstraction:
- **VehicleBase** (abstract parent)

### Subclasses:
- **Car**
- **Truck**

### Composition:
- **VehicleBase has an Engine** (composition in OOP and FK relationship in DB)

---

## 3) SOLID Principles (Where and How)

### ✅ SRP — Single Responsibility Principle
Each layer has one responsibility:
- **Controller**: handles user interaction (CLI/menu only), no business logic.
- **Service**: validation + business rules + exceptions.
- **Repository**: JDBC operations only (CRUD), no business logic.
- **Utils**: helper tools (sorting, reflection), no persistence.

### ✅ OCP — Open/Closed Principle
The system is open for extension:
- New vehicle types can be added by creating a new subclass of `VehicleBase`
- Existing service/controller logic remains unchanged (polymorphism handles it)

### ✅ LSP — Liskov Substitution Principle
`Car` and `Truck` can be used through `VehicleBase` references safely:
- Both override required methods (`calculateRentalCost`, `getType`)
- They behave correctly when treated as `VehicleBase`

### ✅ ISP — Interface Segregation Principle
Interfaces are clean and focused:
- `CrudRepository<T>` includes only CRUD operations
- `VehicleService` defines only service-level actions
  No “fat interface” forcing classes to implement unused methods.

### ✅ DIP — Dependency Inversion Principle
High-level modules depend on abstractions:
- `VehicleServiceImpl` depends on `CrudRepository<VehicleBase>` (interface)
- The repository implementation is injected via constructor (constructor injection)

---

## 4) Advanced OOP Features (Proof)

### ✅ Generics
A generic repository interface is used:
- `CrudRepository<T>` provides `create/findAll/findById/update/delete`

### ✅ Lambdas
Lambda expressions are used for:
- Sorting vehicles by `pricePerDay` using `Comparator`
- Filtering vehicles using `stream().filter(...)`

Utility class: `SortingUtils`

### ✅ Reflection / RTTI
A reflection utility prints runtime information:
- Class name
- Fields list
- Methods list

Utility class: `ReflectionUtils`

### ✅ Polymorphism
A single list of `VehicleBase` can store:
- `Car`
- `Truck`

Overridden methods are called dynamically at runtime.

---

## 5) Architecture Explanation (Layers)

### Controller Layer
- Calls service methods only
- No database or validation logic inside controller

### Service Layer
- Validation (input checks)
- Business rules (duplicate checking, ID validation)
- Throws domain exceptions
- Uses lambdas via SortingUtils

### Repository Layer
- Implements `CrudRepository<VehicleBase>`
- Uses JDBC (PreparedStatement/ResultSet)
- Executes SQL against PostgreSQL
- No business logic

### Database
- PostgreSQL schema contains multiple related tables with FK

---

## 6) Database Schema (PostgreSQL)

Tables:
- `vehicles` (parent)
- `engines` (FK → vehicles.id)
- `car_details` (optional subclass table)
- `truck_details` (optional subclass table)

Schema file:
- `src/main/resources/schema.sql`

---

## 7) How To Run (Step-by-step)

### 1) Create database
Create a PostgreSQL database named:
- `car_rental_db`

### 2) Run schema
Open `src/main/resources/schema.sql` and execute it in pgAdmin Query Tool.

### 3) Configure DB connection
Update credentials in:
- `src/main/java/DatabaseConnection.java`

```java
private static final String URL = "jdbc:postgresql://localhost:5432/car_rental_db";
private static final String USER = "postgres";
private static final String PASS = "your_password";
