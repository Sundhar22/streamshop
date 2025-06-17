package com.streamshop.inventory_service.service;

import java.util.*;


import com.streamshop.inventory_service.presentation.DTO.BulkAvailabilityRequest;
import com.streamshop.inventory_service.presentation.DTO.BulkAvailabilityResponse;
import com.streamshop.inventory_service.presentation.DTO.CreateInventoryRequest;
import com.streamshop.inventory_service.presentation.DTO.InventoryResponse;
import com.streamshop.inventory_service.presentation.DTO.ReserveInventoryRequest;

public interface InventoryService {
  public InventoryResponse getInventoryItem(UUID productId);

  public InventoryResponse saveInventory(CreateInventoryRequest request);

  public void reserveInventory(ReserveInventoryRequest request);

  public List<InventoryResponse> getAllInventoryItems(boolean lowStockOnly, int page, int size);

  public void releaseInventory(ReserveInventoryRequest request);

  public void consumeInventory(ReserveInventoryRequest request);

  public void restockInventory(ReserveInventoryRequest request);

  public BulkAvailabilityResponse checkBulkAvailability(BulkAvailabilityRequest request);
}
