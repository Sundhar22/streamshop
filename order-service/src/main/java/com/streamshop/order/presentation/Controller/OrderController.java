
package com.streamshop.order.presentation.Controller;

import java.util.List;
import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.streamshop.order.presentation.dto.CreateOrderRequest;
import com.streamshop.order.presentation.dto.OrderResponse;
import com.streamshop.order.presentation.dto.UpdateStatusRequest;
import com.streamshop.order.service.OrderService;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;

@RestController
@RequestMapping("/api/v1/orders")
@RequiredArgsConstructor
public class OrderController {
  private final OrderService orderService;

  @PostMapping
  public ResponseEntity<OrderResponse> createOrder(@RequestBody CreateOrderRequest request) {
    OrderResponse orderResponse = orderService.createOrder(request);
    return ResponseEntity.ok(orderResponse);
  }

  @GetMapping("/{orderId}")
  public ResponseEntity<OrderResponse> getOrderById(@PathVariable String orderId) {
    OrderResponse orderResponse = orderService.getOrderById(getUuidFromPath(orderId));
    return ResponseEntity.ok(orderResponse);
  }

  @GetMapping("/user/{userId}")
  public ResponseEntity<List<OrderResponse>> getOrdersByUserId(@PathVariable String userId) {
    List<OrderResponse> orderResponses = orderService.getOrdersByUserId(getUuidFromPath(userId));
    return ResponseEntity.ok(orderResponses);
  }

  @PutMapping("/{orderId}/status")
  // @PreAuthorize("hasRole('ADMIN')")
public ResponseEntity<Void> UpdateOrderStatus(@PathVariable String orderId, @RequestBody UpdateStatusRequest request) {
    try {

        orderService.updateOrderStatus(getUuidFromPath(orderId), request.getStatus());
        return ResponseEntity.ok().build();
    } catch (IllegalArgumentException e) {
        return ResponseEntity.badRequest().body(null); // Invalid UUID
    }
}

  @PostMapping("/{orderId}/cancel")
  public ResponseEntity<Void> cancelOrder(@PathVariable String orderId) {
    
    orderService.cancelOrder(getUuidFromPath(orderId));
    return ResponseEntity.ok().build();
  }

  @PostMapping("/{orderId}/return")
  public ResponseEntity<Void> returnOrder(@PathVariable String orderId) {
    orderService.returnOrder(getUuidFromPath(orderId));
    return ResponseEntity.ok().build();
  }

private UUID getUuidFromPath(String id){

        String formattedId = id.replaceFirst(
            "(\\p{XDigit}{8})(\\p{XDigit}{4})(\\p{XDigit}{4})(\\p{XDigit}{4})(\\p{XDigit}+)",
            "$1-$2-$3-$4-$5"
        );
        return UUID.fromString(formattedId);
  }

}
