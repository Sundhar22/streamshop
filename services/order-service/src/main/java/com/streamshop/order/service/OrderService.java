
package com.streamshop.order.service;

import java.util.List;
import java.util.UUID;

import com.streamshop.order.presentation.dto.CreateOrderRequest;
import com.streamshop.order.presentation.dto.OrderResponse;

public interface OrderService {

  public OrderResponse createOrder(CreateOrderRequest request);

  public OrderResponse getOrderById(UUID orderId);

  public List<OrderResponse> getOrdersByUserId(UUID userId);

  public void updateOrderStatus(UUID orderId, String status);

  public void cancelOrder(UUID orderId);

  public void returnOrder(UUID orderId);

}
