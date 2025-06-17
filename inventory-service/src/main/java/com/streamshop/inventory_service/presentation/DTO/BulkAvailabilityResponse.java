package com.streamshop.inventory_service.presentation.DTO;

import java.util.List;
import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BulkAvailabilityResponse {

  private boolean allAvailable;
  private List<ItemAvailability> results;

  @Data
  @AllArgsConstructor
  public static class ItemAvailability {
    private UUID productId;
    private boolean available;
    private int availableQuantity;
  }
}
