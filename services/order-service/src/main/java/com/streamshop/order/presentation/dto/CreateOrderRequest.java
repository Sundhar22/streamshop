package com.streamshop.order.presentation.dto;

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
public class CreateOrderRequest {
  private UUID userId;
  private List<OrderItemRequest> items;
  private Map<String, Object> shippingAddress; // For JSONB
  private UUID paymentId;
}
