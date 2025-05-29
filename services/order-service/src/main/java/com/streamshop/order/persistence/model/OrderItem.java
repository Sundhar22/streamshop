package com.streamshop.order.persistence.model;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;

@Entity
@Table(name = "order_items")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderItem {

  @EmbeddedId
  private OrderItemId id;

  @ManyToOne(fetch = FetchType.LAZY)
  @MapsId("orderId")
  @JoinColumn(name = "order_id")
  private Order order;

  @Column(name = "quantity", nullable = false)
  private int quantity;

  @Column(name = "price", precision = 12, scale = 2, nullable = false)
  private BigDecimal price;
}
