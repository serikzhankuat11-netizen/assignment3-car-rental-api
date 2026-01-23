
PRAGMA foreign_keys = ON;

CREATE TABLE IF NOT EXISTS customers (
  id INTEGER PRIMARY KEY AUTOINCREMENT,
  full_name TEXT NOT NULL UNIQUE
);

CREATE TABLE IF NOT EXISTS vehicles (
  id INTEGER PRIMARY KEY AUTOINCREMENT,
  name TEXT NOT NULL,
  price_per_day REAL NOT NULL,
  type TEXT NOT NULL CHECK(type IN ('CAR','TRUCK')),
  seats INTEGER,
  capacity_tons REAL
);

CREATE TABLE IF NOT EXISTS rentals (
  id INTEGER PRIMARY KEY AUTOINCREMENT,
  customer_id INTEGER NOT NULL,
  vehicle_id INTEGER NOT NULL,
  days INTEGER NOT NULL CHECK(days > 0),
  start_date TEXT NOT NULL,
  FOREIGN KEY(customer_id) REFERENCES customers(id) ON DELETE RESTRICT,
  FOREIGN KEY(vehicle_id) REFERENCES vehicles(id) ON DELETE RESTRICT
);

-- Sample inserts (optional)
INSERT OR IGNORE INTO customers(full_name) VALUES ('Ali Nur'), ('Zhankuat Serik');

INSERT OR IGNORE INTO vehicles(name, price_per_day, type, seats, capacity_tons)
VALUES
('Toyota Camry', 15000, 'CAR', 5, NULL),
('Hyundai Accent', 12000, 'CAR', 5, NULL),
('Volvo FH', 30000, 'TRUCK', NULL, 15.0);
