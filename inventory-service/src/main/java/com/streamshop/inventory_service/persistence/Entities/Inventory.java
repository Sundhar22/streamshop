
package com.streamshop.inventory_service.persistence.Entities;

import java.time.LocalDateTime;
import java.util.UUID;

import org.hibernate.annotations.UpdateTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "inventory")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Inventory {

  @Id
  @Column(name = "product_id", columnDefinition = "CHAR(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci", nullable = false)
  private String productId;

  @Column(name = "quantity")
  private int quantity;

  @Column(name = "reserved_quantity")
  private int reservedQuantity;

  @Column(name = "low_stock_threshold")
  private int lowStockThreshold;

  @Column(name = "last_restocked_at")
  private LocalDateTime lastRestockedAt;

  @UpdateTimestamp
  @Column(name = "updated_at")
  private LocalDateTime updatedAt;

  // Convert to UUID if needed
  @Transient
  public UUID getProductIdAsUUID() {
    return productId != null ? UUID.fromString(productId) : null;
  }

  // Ensure UUID is stored as string
  public void setProductIdFromUUID(UUID uuid) {
    this.productId = uuid != null ? uuid.toString() : null;
  }

  @Transient
  public int getAvailableQuantity() {
    return quantity - reservedQuantity;
  }
}
