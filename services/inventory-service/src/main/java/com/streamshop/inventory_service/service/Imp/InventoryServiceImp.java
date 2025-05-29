
package com.streamshop.inventory_service.service.Imp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.streamshop.inventory_service.Expections.InsufficientInventoryException;
import com.streamshop.inventory_service.Expections.NotFoundException;
import com.streamshop.inventory_service.persistence.Entities.Inventory;
import com.streamshop.inventory_service.persistence.Repositories.InventoryRepository;
import com.streamshop.inventory_service.presentation.DTO.BulkAvailabilityRequest;
import com.streamshop.inventory_service.presentation.DTO.BulkAvailabilityResponse;
import com.streamshop.inventory_service.presentation.DTO.CreateInventoryRequest;
import com.streamshop.inventory_service.presentation.DTO.InventoryResponse;
import com.streamshop.inventory_service.presentation.DTO.ReserveInventoryRequest;
import com.streamshop.inventory_service.service.InventoryService;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class InventoryServiceImp implements InventoryService {
  private final InventoryRepository repository;

  @Override
  public InventoryResponse getInventoryItem(UUID productId) {
    Inventory item = repository.findById(productId.toString())
        .orElseThrow(() -> new NotFoundException("Inventory item not found"));
    return mapToResponse(item);
  }

  @Override
  public InventoryResponse saveInventory(CreateInventoryRequest request) {
    Inventory item = new Inventory();
    item.setProductIdFromUUID(request.getProductId());
    item.setQuantity(request.getQuantity());
    item.setLowStockThreshold(request.getLowStockThreshold());
    Inventory savedItem = repository.save(item);
    return mapToResponse(savedItem);
  }

  @Override
  public void reserveInventory(ReserveInventoryRequest request) {
    Inventory item = repository.findById(request.getProductId().toString())
        .orElseThrow(() -> new NotFoundException("Inventory item not found"));

    int available = item.getQuantity() - item.getReservedQuantity();
    if (available < request.getQuantity()) {
      throw new InsufficientInventoryException("Not enough available inventory");
    }

    item.setReservedQuantity(item.getReservedQuantity() + request.getQuantity());
    repository.save(item);
  }

  @Override
  public List<InventoryResponse> getAllInventoryItems(boolean lowStockOnly, int page, int size) {
    if (lowStockOnly) {
      return repository.findLowStockItems().stream()
          .map(this::mapToResponse)
          .collect(Collectors.toList());
    } else {
      Page<Inventory> pagedResult = repository.findAll(PageRequest.of(page, size));
      return pagedResult.getContent().stream()
          .map(this::mapToResponse)
          .collect(Collectors.toList());
    }
  }

  @Override
  public void releaseInventory(ReserveInventoryRequest request) {
    Inventory item = repository.findById(request.getProductId().toString())
        .orElseThrow(() -> new NotFoundException("Inventory item not found"));

    if (item.getReservedQuantity() < request.getQuantity()) {
      throw new IllegalArgumentException("Cannot release more than reserved quantity");
    }

    item.setReservedQuantity(item.getReservedQuantity() - request.getQuantity());
    repository.save(item);
  }

  @Override
  public void consumeInventory(ReserveInventoryRequest request) {
    Inventory item = repository.findById(request.getProductId().toString())
        .orElseThrow(() -> new NotFoundException("Inventory item not found"));

    if (item.getReservedQuantity() < request.getQuantity() ||
        item.getQuantity() < request.getQuantity()) {
      throw new IllegalArgumentException("Insufficient quantity to consume");
    }

    item.setQuantity(item.getQuantity() - request.getQuantity());
    item.setReservedQuantity(item.getReservedQuantity() - request.getQuantity());
    repository.save(item);
  }

  @Override
  public void restockInventory(ReserveInventoryRequest request) {
    Inventory item = repository.findById(request.getProductId().toString())
        .orElseThrow(() -> new NotFoundException("Inventory item not found"));

    item.setQuantity(item.getQuantity() + request.getQuantity());
    item.setLastRestockedAt(LocalDateTime.now());
    repository.save(item);
  }

  @Override
  public BulkAvailabilityResponse checkBulkAvailability(BulkAvailabilityRequest request) {
    List<BulkAvailabilityResponse.ItemAvailability> results = new ArrayList<>();
    boolean allAvailable = true;

    for (BulkAvailabilityRequest.InventoryCheckItem checkItem : request.getItems()) {
      Inventory item = repository.findById(checkItem.getProductId().toString())
          .orElseThrow(() -> new NotFoundException("Item not found: " + checkItem.getProductId()));

      int available = item.getQuantity() - item.getReservedQuantity();
      boolean itemAvailable = available >= checkItem.getQuantity();

      if (!itemAvailable)
        allAvailable = false;

      results.add(new BulkAvailabilityResponse.ItemAvailability(
          checkItem.getProductId(),
          itemAvailable,
          available));
    }

    return new BulkAvailabilityResponse(allAvailable, results);
  }

  private InventoryResponse mapToResponse(Inventory item) {
    InventoryResponse response = new InventoryResponse();
    response.setProductId(item.getProductIdAsUUID());
    response.setQuantity(item.getQuantity());
    response.setReservedQuantity(item.getReservedQuantity());
    response.setLowStockThreshold(item.getLowStockThreshold());
    response.setLastRestockedAt(item.getLastRestockedAt());
    response.setAvailableQuantity(item.getAvailableQuantity());
    return response;
  }
}
