package com.streamshop.order.presentation.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderResponse {
  private UUID orderId;
  private UUID userId;
  private BigDecimal total;
  private String status;
  private LocalDateTime createdAt;
  private Map<String, Object> shippingAddress;
  private UUID paymentId;
  private List<OrderItemResponse> items;
}
