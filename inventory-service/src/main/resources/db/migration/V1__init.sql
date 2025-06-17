CREATE TABLE inventory (
  product_id CHAR(36) NOT NULL,
  quantity INT NOT NULL CHECK (quantity >= 0),
  reserved_quantity INT DEFAULT 0 CHECK (reserved_quantity >= 0),
  low_stock_threshold INT DEFAULT 10 CHECK (low_stock_threshold >= 0),
  last_restocked_at TIMESTAMP NULL DEFAULT NULL,
  updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (product_id),
  CHECK (reserved_quantity <= quantity)
);

ALTER DATABASE inventorydb CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
ALTER TABLE inventory MODIFY product_id CHAR(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
