# 🚗 Car Rental API — Assignment 3  
### Advanced OOP · JDBC · Exception Handling · Multi-layer Architecture

This project is developed as part of **DBMS Assignment 3**.  
It demonstrates a complete **Java OOP API** with real **SQLite database**, full **CRUD operations**, 
**custom exceptions**, and **clean multi-layer architecture**.

---

# 📌 1. Project Overview

The **Car Rental API** simulates a simple rental system with customers, vehicles, and rental records.  
The application is implemented in **pure Java (JDK 17)** using:

- Object-Oriented Programming  
- JDBC with PreparedStatement  
- Multi-layer architecture  
- Custom exception hierarchy  
- Data validation & business rules  
- Relational database (SQLite)

All interactions happen through a clean **CLI menu** (Command Line Interface).

---

# 📦 2. Project Architecture

```
controller/       → CLI controller (Main)
service/          → Business logic (validation, rules)
repository/       → JDBC CRUD operations
model/            → OOP entities & inheritance
exception/        → Custom exceptions
utils/            → DB connection & schema initialization
resources/        → schema.sql
data/             → SQLite database (auto-created)
```

---

# 🧬 3. OOP Design

### ✔ Abstract Class  
**`BaseEntity`**  
- Fields: `id`, `name`
- 2 abstract methods: `describe()`, `validate()`
- 1 concrete method: `displayName()`

### ✔ Inheritance  
- `Customer extends BaseEntity`  
- `VehicleBase extends BaseEntity`
  - `Car extends VehicleBase`
  - `Truck extends VehicleBase`

### ✔ Interfaces  
- `Validatable` → `void validate()`
- `PricedItem` → `double getPricePerDay()`

### ✔ Polymorphism  
`VehicleBase` reference is used for:  
- `calculateRent(days)` (Truck overrides with +20%)  
- `getType()`  
- `describe()`

### ✔ Composition  
`Rental` **has-a** `Customer` + `VehicleBase`.

---

# 🗄 4. Database

The project uses **SQLite** (lightweight, file-based DB).  
Database file is created automatically:

```
data/car_rental.db
```

### Tables:
- `customers`
- `vehicles` (supports both Car & Truck via fields)
- `rentals` (FK → customers, vehicles)

### Schema file:
```
src/main/resources/schema.sql
```

---

# ⚙️ 5. How To Run

## ▶ A) IntelliJ IDEA  
1. **File → Open** → select project folder  
2. Ensure **JDK 17** is selected  
3. Maven will auto-load dependencies  
4. Run:  
   ```
   aitu.controller.Main
   ```

## ▶ B) Terminal (optional)
```bash
mvn clean package
java -jar target/assignment3-car-rental-api-1.0.0.jar
```

---

# 🧪 6. Features Demonstrated

### ✔ CRUD Operations
- Add / list / update / delete customers  
- Add / list / update / delete vehicles  
- Add / list / update / delete rental records  

### ✔ Exception Handling  
- `InvalidInputException`  
- `DuplicateResourceException`  
- `ResourceNotFoundException`  
- `DatabaseOperationException`

### ✔ Business Rules  
- Customer full name cannot be empty  
- Vehicle price/day > 0  
- Truck rent = price × days × 1.2  
- Days must be > 0  
- Start date must be ISO format  

---

# 🖥 7. Menu Demo

```
=== Car Rental API (CLI demo) ===
1) List customers
2) Add customer
3) Update customer
4) Delete customer
5) List vehicles (polymorphism demo)
6) Add vehicle
...
```



# 💭 9. Reflection (example)

During this assignment, I learned how to combine **OOP principles** with a real **database layer**,  
using JDBC safely through **prepared statements**.  
I also understood the importance of separating code into **service**, **repository**, and **controller** layers.  
Implementing custom exceptions helped me design cleaner and more predictable error-handling logic.

---

# ✅ 10. Conclusion

The Car Rental API fully satisfies all assignment requirements:  
✔ Abstract classes · ✔ Inheritance · ✔ Interfaces  
✔ Polymorphism · ✔ Composition  
✔ JDBC CRUD · ✔ Multi-layer architecture  
✔ Custom exception hierarchy  
✔ Complete README

> **This project is ready for submission on Moodle & GitHub.**
