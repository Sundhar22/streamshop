package com.streamshop.order.presentation.dto.FeignDto.InventoryDTO;

import lombok.Data;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InventoryCheckItem {
  private String productId; // UUID as String
  private int quantity;
}
