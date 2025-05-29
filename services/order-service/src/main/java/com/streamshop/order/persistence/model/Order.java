package com.streamshop.order.persistence.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;

import com.streamshop.order.mapper.JsonToMapConverter;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "orders")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Order {

  @Id
  @Column(columnDefinition = "BINARY(16)", nullable = false)
  private UUID id;

  @Column(name = "user_id", columnDefinition = "BINARY(16)", nullable = false)
  private UUID userId;

  @Column(name = "total", precision = 12, scale = 2, nullable = false)
  private BigDecimal total;

  @Column(name = "status", length = 20, nullable = false)
  private String status;

  @Column(name = "created_at", columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
  private LocalDateTime createdAt;

  @Column(name = "shipping_address", columnDefinition = "jsonb")
  @Convert(converter = JsonToMapConverter.class)
  private Map<String, Object> shippingAddress;

  @Column(name = "payment_id", columnDefinition = "BINARY(16)", nullable = false, unique = true)
  private UUID paymentId;

  @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
  private List<OrderItem> items;

  @PrePersist
  public void prePersist() {
    if (createdAt == null) {
      createdAt = LocalDateTime.now();
    }

    if (items == null) {
      items = new ArrayList<>();
    }
  }
}
