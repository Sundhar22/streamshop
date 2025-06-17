package com.streamshop.order.persistence.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.streamshop.order.persistence.model.OrderItem;
import com.streamshop.order.persistence.model.OrderItemId;

import java.util.List;
import java.util.UUID;

@Repository
public interface OrderItemRepository extends JpaRepository<OrderItem, OrderItemId> {
  List<OrderItem> findByOrderId(UUID orderId);
}
