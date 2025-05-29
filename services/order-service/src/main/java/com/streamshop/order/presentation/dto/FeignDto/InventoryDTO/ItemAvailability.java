package com.streamshop.order.presentation.dto.FeignDto.InventoryDTO;

import lombok.Data;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ItemAvailability {
  private String productId; // UUID as String
  private boolean available;
  private int availableQuantity;
}
