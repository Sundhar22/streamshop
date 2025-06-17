CREATE TABLE orders (
    id BINARY(16) PRIMARY KEY,
    user_id BINARY(16) NOT NULL,
    total DECIMAL(12,2) NOT NULL,
    status VARCHAR(20) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    shipping_address JSON,
    payment_id BINARY(16) NOT NULL,
    CONSTRAINT uk_orders_payment_id UNIQUE (payment_id)
);

CREATE TABLE order_items (
    order_id BINARY(16) NOT NULL,
    product_id BINARY(16) NOT NULL,
    quantity INT NOT NULL,
    price DECIMAL(12,2) NOT NULL,
    PRIMARY KEY (order_id, product_id),
    FOREIGN KEY (order_id) REFERENCES orders(id)
);


