package com.streamshop.order.Client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.streamshop.order.presentation.dto.FeignDto.InventoryDTO.BulkAvailabilityRequest;
import com.streamshop.order.presentation.dto.FeignDto.InventoryDTO.BulkAvailabilityResponse;
import com.streamshop.order.presentation.dto.FeignDto.InventoryDTO.ReserveInventoryRequest;

@FeignClient(name = "inventory", url = "localhost:8082")
public interface InventoryClient {

  @PostMapping("/api/v1/inventory/release")
  void releaseInventory(@RequestBody ReserveInventoryRequest request);

  @PostMapping("/api/v1/inventory/restock")
  void restockInventory(@RequestBody ReserveInventoryRequest request);

  @PostMapping("/api/v1/inventory/check-availability")
  BulkAvailabilityResponse checkBulkAvailability(@RequestBody BulkAvailabilityRequest request);

  @PostMapping("/api/v1/inventory/reserve")
  void reserveInventory(@RequestBody ReserveInventoryRequest request);

  @PostMapping("/api/v1/inventory/consume")
  void consumeInventory(@RequestBody ReserveInventoryRequest request);
}
