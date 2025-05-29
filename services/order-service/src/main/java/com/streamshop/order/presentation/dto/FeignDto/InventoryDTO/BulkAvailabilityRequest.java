package com.streamshop.order.presentation.dto.FeignDto.InventoryDTO;

import lombok.Data;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BulkAvailabilityRequest {
  private List<InventoryCheckItem> items;
}
