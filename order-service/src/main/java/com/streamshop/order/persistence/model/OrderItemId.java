package com.streamshop.order.persistence.model;

import java.io.Serializable;
import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Embeddable
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderItemId implements Serializable {
  @Column(name = "order_id", columnDefinition = "BINARY(16)") // Match Order.id
  private UUID orderId;
  @Column(name = "product_id") // Explicit mapping
  private UUID productId;

}
