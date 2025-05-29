package com.streamshop.order.persistence.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.streamshop.order.persistence.model.Order;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface OrderRepository extends JpaRepository<Order, UUID> {
  Optional<Order> findByPaymentId(UUID paymentId);

  List<Order> findByUserId(UUID userId);
}
