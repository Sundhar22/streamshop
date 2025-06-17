package com.streamshop.inventory_service.presentation.Controller;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.streamshop.inventory_service.Expections.InsufficientInventoryException;
import com.streamshop.inventory_service.presentation.DTO.BulkAvailabilityRequest;
import com.streamshop.inventory_service.presentation.DTO.BulkAvailabilityResponse;
import com.streamshop.inventory_service.presentation.DTO.CreateInventoryRequest;
import com.streamshop.inventory_service.presentation.DTO.InventoryResponse;
import com.streamshop.inventory_service.presentation.DTO.ReserveInventoryRequest;
import com.streamshop.inventory_service.service.InventoryService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/inventory")
@RequiredArgsConstructor
public class InventoryController {
  @Autowired
  private final InventoryService inventoryService;

  @GetMapping("/{productId}")
  public ResponseEntity<InventoryResponse> getInventoryItem(@PathVariable String productId) {

    String sanitizedId = productId.replaceAll("[{}]", "");
    UUID uuid = UUID.fromString(sanitizedId);
    return ResponseEntity.ok(inventoryService.getInventoryItem(uuid));
  }

  @PostMapping
  public ResponseEntity<InventoryResponse> createInventoryItem(@RequestBody CreateInventoryRequest request) {
    return ResponseEntity.ok(inventoryService.saveInventory(request));
  }

  @GetMapping
  public ResponseEntity<List<InventoryResponse>> getAllInventoryItems(
      @RequestParam(required = false) boolean lowStockOnly,
      @RequestParam(defaultValue = "0") int page,
      @RequestParam(defaultValue = "20") int size) {
    return ResponseEntity.ok(inventoryService.getAllInventoryItems(lowStockOnly, page, size));
  }

  @PostMapping("/reserve")
  public ResponseEntity<Void> reserveInventory(@RequestBody ReserveInventoryRequest request) {

    inventoryService.reserveInventory(request);
    return ResponseEntity.ok().build();
  }

  @PostMapping("/release")
  public ResponseEntity<Void> releaseInventory(@RequestBody ReserveInventoryRequest request) {
    inventoryService.releaseInventory(request);
    return ResponseEntity.ok().build();
  }

  @PostMapping("/consume")
  public ResponseEntity<Void> consumeInventory(@RequestBody ReserveInventoryRequest request) {
    inventoryService.consumeInventory(request);
    return ResponseEntity.ok().build();
  }

  @PostMapping("/restock")
  public ResponseEntity<Void> restockInventory(@RequestBody ReserveInventoryRequest request) {
    inventoryService.restockInventory(request);
    return ResponseEntity.ok().build();
  }

  @PostMapping("/check-availability")
  public ResponseEntity<BulkAvailabilityResponse> checkBulkAvailability(@RequestBody BulkAvailabilityRequest request) {
    return ResponseEntity.ok(inventoryService.checkBulkAvailability(request));
  }

  @ExceptionHandler(NotFoundException.class)
  public ResponseEntity<String> handleNotFound(NotFoundException ex) {
    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
  }

  @ExceptionHandler({ InsufficientInventoryException.class, IllegalArgumentException.class })
  public ResponseEntity<String> handleBadRequest(RuntimeException ex) {
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
  }

}
