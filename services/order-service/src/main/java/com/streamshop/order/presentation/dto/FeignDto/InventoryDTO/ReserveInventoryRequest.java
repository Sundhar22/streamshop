package com.streamshop.order.presentation.dto.FeignDto.InventoryDTO;

import lombok.Data;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReserveInventoryRequest {
  private String productId; 
  private int quantity;
}
