DROP TABLE IF EXISTS truck_details;
DROP TABLE IF EXISTS car_details;
DROP TABLE IF EXISTS engines;
DROP TABLE IF EXISTS vehicles;

CREATE TABLE vehicles (
    id SERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    price_per_day DECIMAL(10,2) NOT NULL,
    type VARCHAR(20) NOT NULL
);

CREATE TABLE engines (
    id SERIAL PRIMARY KEY,
    vehicle_id INT NOT NULL REFERENCES vehicles(id) ON DELETE CASCADE,
    model VARCHAR(50) NOT NULL,
    horsepower INT NOT NULL
);

CREATE TABLE car_details (
    vehicle_id INT PRIMARY KEY REFERENCES vehicles(id) ON DELETE CASCADE,
    seats INT NOT NULL
);

CREATE TABLE truck_details (
    vehicle_id INT PRIMARY KEY REFERENCES vehicles(id) ON DELETE CASCADE,
    cargo_capacity_kg DECIMAL(10,2) NOT NULL
);

-- sample inserts
INSERT INTO vehicles(name, price_per_day, type) VALUES ('Honda Civic', 70, 'CAR');
INSERT INTO engines(vehicle_id, model, horsepower) VALUES (1, 'R18A', 140);
INSERT INTO car_details(vehicle_id, seats) VALUES (1, 5);

INSERT INTO vehicles(name, price_per_day, type) VALUES ('MAN Truck', 150, 'TRUCK');
INSERT INTO engines(vehicle_id, model, horsepower) VALUES (2, 'Diesel', 450);
INSERT INTO truck_details(vehicle_id, cargo_capacity_kg) VALUES (2, 2000);
