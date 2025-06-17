package com.streamshop.order.service.imp;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.*;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.streamshop.order.Client.InventoryClient;
import com.streamshop.order.Client.UserServiceClient;
import com.streamshop.order.persistence.model.Order;
import com.streamshop.order.persistence.model.OrderItem;
import com.streamshop.order.persistence.model.OrderItemId;
import com.streamshop.order.persistence.repository.OrderItemRepository;
import com.streamshop.order.persistence.repository.OrderRepository;
import com.streamshop.order.presentation.dto.CreateOrderRequest;
import com.streamshop.order.presentation.dto.OrderItemRequest;
import com.streamshop.order.presentation.dto.OrderItemResponse;
import com.streamshop.order.presentation.dto.OrderResponse;
import com.streamshop.order.presentation.dto.FeignDto.UserResponse;
import com.streamshop.order.presentation.dto.FeignDto.InventoryDTO.BulkAvailabilityRequest;
import com.streamshop.order.presentation.dto.FeignDto.InventoryDTO.BulkAvailabilityResponse;
import com.streamshop.order.presentation.dto.FeignDto.InventoryDTO.InventoryCheckItem;
import com.streamshop.order.presentation.dto.FeignDto.InventoryDTO.ReserveInventoryRequest;
import com.streamshop.order.service.OrderService;
import com.streamshop.order.service.events.OrderPlacedEvents;

import jakarta.transaction.Transactional;

@Service
public class OrderServiceImp implements OrderService {
  @ResponseStatus(HttpStatus.NOT_ACCEPTABLE)
  public class OrderNotAcceptedException extends RuntimeException {
    public OrderNotAcceptedException(String message) {
      super(message);
    }
  }

  @Autowired
  private OrderRepository orderRepository;

  @Autowired
  private OrderItemRepository orderItemRepository;

  @Autowired
  private InventoryClient inventoryClient;

  @Autowired
  private UserServiceClient userServiceClient;

  @Autowired
  private KafkaTemplate<String, OrderPlacedEvents> kafkaTemplate;

  @Value("${spring.kafka.template.default-topic}")
  private String topic;

  @Override
  @Transactional
  public OrderResponse createOrder(CreateOrderRequest request) {
    // Validate request
    validateOrderRequest(request);
    try {
      // Convert Order Items to Inventory Check Requests
      List<InventoryCheckItem> inventoryCheckItems = request.getItems().stream()
          .map(item -> new InventoryCheckItem(item.getProductId().toString(), item.getQuantity()))
          .collect(Collectors.toList());

      // Check Inventory Availability
      BulkAvailabilityRequest bulkAvailabilityRequest = new BulkAvailabilityRequest(inventoryCheckItems);
      BulkAvailabilityResponse availabilityResponse = inventoryClient.checkBulkAvailability(bulkAvailabilityRequest);

      if (!availabilityResponse.isAllAvailable()) {
        throw new RuntimeException("One or more items are not available");
      }

      // Reserve Inventory
      for (OrderItemRequest item : request.getItems()) {
        ReserveInventoryRequest reserveRequest = new ReserveInventoryRequest();
        reserveRequest.setProductId(item.getProductId().toString());
        reserveRequest.setQuantity(item.getQuantity());
        inventoryClient.reserveInventory(reserveRequest);
      }

      // Calculate total order value
      BigDecimal total = request.getItems().stream()
          .map(item -> item.getPrice().multiply(BigDecimal.valueOf(item.getQuantity())))
          .reduce(BigDecimal.ZERO, BigDecimal::add);

      // Create Order
      UUID orderId = UUID.randomUUID();
      Order order = Order.builder()
          .id(orderId)
          .userId(request.getUserId())
          .total(total)
          .status("CREATED")
          .createdAt(LocalDateTime.now())
          .shippingAddress(request.getShippingAddress())
          .paymentId(request.getPaymentId())
          .build();

      orderRepository.save(order);

      // Create and Save Order Items
      List<OrderItem> orderItems = request.getItems().stream()
          .map(item -> {
            OrderItem orderItem = new OrderItem();
            orderItem.setId(new OrderItemId(orderId, item.getProductId()));
            orderItem.setPrice(item.getPrice());
            orderItem.setQuantity(item.getQuantity());
            orderItem.setOrder(order);
            return orderItem;
          }).toList();

      orderItemRepository.saveAll(orderItems);

      // Build Response
      List<OrderItemResponse> itemResponses = orderItems.stream()
          .map(item -> new OrderItemResponse(item.getId().getProductId(), item.getQuantity(), item.getPrice()))
          .collect(Collectors.toList());

      UserResponse user = userServiceClient.getUserById(request.getUserId().toString());

      OrderPlacedEvents orderPlacedEvents = new OrderPlacedEvents(order.getId().toString(), user.getEmail());
      kafkaTemplate.send(topic, orderPlacedEvents);

      return OrderResponse.builder()
          .orderId(order.getId())
          .userId(order.getUserId())
          .total(order.getTotal())
          .status(order.getStatus())
          .createdAt(order.getCreatedAt())
          .shippingAddress(order.getShippingAddress())
          .paymentId(order.getPaymentId())
          .items(itemResponses)
          .build();

    } catch (Exception e) {
      // Handle exceptions and rollback inventory reservations if needed
      throw new RuntimeException("Failed to create order: " + e.getMessage(), e);
    }
  }

  @Override
  public OrderResponse getOrderById(UUID orderId) {
    Order order = orderRepository.findById(orderId)
        .orElseThrow(() -> new RuntimeException("Order not found"));
    List<OrderItem> orderItems = order.getItems();

    List<OrderItemResponse> itemResponses = orderItems.stream()
        .map(item -> new OrderItemResponse(item.getId().getProductId(), item.getQuantity(), item.getPrice()))
        .collect(Collectors.toList());

    return OrderResponse.builder()
        .orderId(order.getId())
        .userId(order.getUserId())
        .total(order.getTotal())
        .status(order.getStatus())
        .createdAt(order.getCreatedAt())
        .shippingAddress(order.getShippingAddress())
        .paymentId(order.getPaymentId())
        .items(itemResponses)
        .build();
  }

  @Override
  public List<OrderResponse> getOrdersByUserId(UUID userId) {
    List<Order> orders = orderRepository.findByUserId(userId);
    return orders.stream()
        .map(order -> {
          List<OrderItem> orderItems = order.getItems();
          List<OrderItemResponse> itemResponses = orderItems.stream()
              .map(item -> new OrderItemResponse(item.getId().getProductId(), item.getQuantity(), item.getPrice()))
              .collect(Collectors.toList());

          return OrderResponse.builder()
              .orderId(order.getId())
              .userId(order.getUserId())
              .total(order.getTotal())
              .status(order.getStatus())
              .createdAt(order.getCreatedAt())
              .shippingAddress(order.getShippingAddress())
              .paymentId(order.getPaymentId())
              .items(itemResponses)
              .build();
        })
        .collect(Collectors.toList());
  }

  @Override
  @Transactional
  public void updateOrderStatus(UUID orderId, String status) {
    Order order = orderRepository.findById(orderId)
        .orElseThrow(() -> new RuntimeException("Order not found"));

    String currentStatus = order.getStatus();

    // Validate transitions
    if ("CANCELLED".equals(status) && !currentStatus.equals("PENDING")) {
      throw new IllegalStateException("Only pending orders can be cancelled");
    }
    if ("RETURNED".equals(status) && !currentStatus.equals("DELIVERED")) {
      throw new IllegalStateException("Only delivered orders can be returned");
    }

    try {
      // Handle status changes
      if ("CANCELLED".equals(status)) {
        // Release inventory
        order.getItems().forEach(item -> {
          ReserveInventoryRequest releaseRequest = new ReserveInventoryRequest();
          releaseRequest.setProductId(item.getId().getProductId().toString());
          releaseRequest.setQuantity(item.getQuantity());
          inventoryClient.releaseInventory(releaseRequest);
        });
      } else if ("RETURNED".equals(status)) {
        // Restock inventory
        order.getItems().forEach(item -> {
          ReserveInventoryRequest restockRequest = new ReserveInventoryRequest();
          restockRequest.setProductId(item.getId().getProductId().toString());
          restockRequest.setQuantity(item.getQuantity());
          inventoryClient.restockInventory(restockRequest);
        });
      } else if ("DELIVERED".equals(status)) {
        // Consume inventory
        order.getItems().forEach(item -> {
          ReserveInventoryRequest consumeRequest = new ReserveInventoryRequest();
          consumeRequest.setProductId(item.getId().getProductId().toString());
          consumeRequest.setQuantity(item.getQuantity());
          inventoryClient.consumeInventory(consumeRequest);
        });
      }

      order.setStatus(status);
      orderRepository.save(order);
    } catch (Exception e) {
      // Compensate on failure
      if ("DELIVERED".equals(status)) {
        // Re-reserve inventory if consumption fails
        order.getItems().forEach(item -> {
          ReserveInventoryRequest reReserveRequest = new ReserveInventoryRequest();
          reReserveRequest.setProductId(item.getId().getProductId().toString());
          reReserveRequest.setQuantity(item.getQuantity());
          inventoryClient.reserveInventory(reReserveRequest);
        });
      }
      throw new RuntimeException("Failed to update order status: " + e.getMessage(), e);
    }
  }

  @Override
  public void cancelOrder(UUID orderId) {
    updateOrderStatus(orderId, "CANCELLED");
  }

  @Override
  public void returnOrder(UUID orderId) {
    updateOrderStatus(orderId, "RETURNED");
  }

  private void validateOrderRequest(CreateOrderRequest request) {
    if (request.getItems() == null || request.getItems().isEmpty()) {
      throw new IllegalArgumentException("Order must contain at least one item");
    }
    if (request.getUserId() == null) {
      throw new IllegalArgumentException("User ID is required");
    }
    if (request.getPaymentId() == null) {
      throw new IllegalArgumentException("Payment ID is required");
    }
    for (OrderItemRequest item : request.getItems()) {
      if (item.getProductId() == null) {
        throw new IllegalArgumentException("Product ID is required");
      }
      if (item.getQuantity() <= 0) {
        throw new IllegalArgumentException("Quantity must be positive for productId: " + item.getProductId());
      }
      if (item.getPrice() == null || item.getPrice().compareTo(BigDecimal.ZERO) < 0) {
        throw new IllegalArgumentException("Price cannot be negative for productId: " + item.getProductId());
      }
    }
  }

}
