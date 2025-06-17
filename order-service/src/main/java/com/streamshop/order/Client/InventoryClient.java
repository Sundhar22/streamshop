package com.streamshop.order.Client;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.service.annotation.PostExchange;

import com.streamshop.order.presentation.dto.FeignDto.InventoryDTO.BulkAvailabilityRequest;
import com.streamshop.order.presentation.dto.FeignDto.InventoryDTO.BulkAvailabilityResponse;
import com.streamshop.order.presentation.dto.FeignDto.InventoryDTO.ReserveInventoryRequest;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;

public interface InventoryClient {

  Logger log = LoggerFactory.getLogger(InventoryClient.class);

  @CircuitBreaker(name = "inventory", fallbackMethod = "fallbackMethod")
  @Retry(name = "inventory")
  @PostExchange("/api/v1/inventory/release")
  void releaseInventory(@RequestBody ReserveInventoryRequest request);

  @CircuitBreaker(name = "inventory", fallbackMethod = "fallbackMethod")
  @Retry(name = "inventory")
  @PostExchange("/api/v1/inventory/restock")
  void restockInventory(@RequestBody ReserveInventoryRequest request);

  @CircuitBreaker(name = "inventory", fallbackMethod = "fallbackBulkMethod")
  @Retry(name = "inventory")
  @PostExchange("/api/v1/inventory/check-availability")
  BulkAvailabilityResponse checkBulkAvailability(@RequestBody BulkAvailabilityRequest request);

  @CircuitBreaker(name = "inventory", fallbackMethod = "fallbackMethod")
  @Retry(name = "inventory")
  @PostExchange("/api/v1/inventory/reserve")
  void reserveInventory(@RequestBody ReserveInventoryRequest request);

  @CircuitBreaker(name = "inventory", fallbackMethod = "fallbackMethod")
  @Retry(name = "inventory")
  @PostExchange("/api/v1/inventory/consume")
  void consumeInventory(@RequestBody ReserveInventoryRequest request);

  default void fallbackMethod(ReserveInventoryRequest request, Throwable throwable) {
    log.info("Cannot get inventory for request product {}, failure reason: {}", request.getProductId(),
        throwable.getMessage());
  }

  default BulkAvailabilityResponse fallbackBulkMethod(BulkAvailabilityRequest request,
      Throwable throwable) {
    log.info("Cannot get inventory for request product {}, failure reason: {}", request.getItems(),
        throwable.getMessage());
    return BulkAvailabilityResponse.builder()
        .allAvailable(false)
        .results(List.of()) // Empty list for results
        .build();
  }
}