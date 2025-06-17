package com.streamshop.inventory_service.presentation.DTO;

import java.util.List;
import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BulkAvailabilityRequest {
  private List<InventoryCheckItem> items;

  @Data
  @Getter
  @Setter
  @NoArgsConstructor
  @AllArgsConstructor
  public static class InventoryCheckItem {
    private UUID productId;
    private int quantity;
  }

}
