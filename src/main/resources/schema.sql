CREATE TABLE menu
(
    dish_id INT NOT NULL UNIQUE AUTO_INCREMENT,
    dish_name VARCHAR(255) NOT NULL,
    category VARCHAR(255) NOT NULL,
    price FLOAT NOT NULL,
    available BOOLEAN NOT NULL,
    PRIMARY KEY (dish_id)
);

CREATE TABLE tables
(
    table_id INT NOT NULL UNIQUE,
    max_seats INT NOT NULL,
    booked BOOLEAN NOT NULL,
    PRIMARY KEY (table_id)
);

CREATE TABLE orders
(
    order_id INT NOT NULL UNIQUE AUTO_INCREMENT,
    table_id INT NOT NULL,
    total FLOAT NOT NULL,
    PRIMARY KEY (order_id),
    FOREIGN KEY (table_id) REFERENCES tables (table_id)
);

CREATE TABLE order_items
(
    order_id INT NOT NULL,
    dish_id INT NOT NULL,
    portions INT NOT NULL,
    FOREIGN KEY (order_id) REFERENCES orders (order_id),
    FOREIGN KEY (dish_id) REFERENCES menu (dish_id)
)