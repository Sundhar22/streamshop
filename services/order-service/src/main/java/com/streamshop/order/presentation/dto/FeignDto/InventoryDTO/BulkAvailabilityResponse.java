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
public class BulkAvailabilityResponse {
  private boolean allAvailable;
  private List<ItemAvailability> results;
}
