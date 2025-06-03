package com.streamshop.product_service.presentation.dto;

import java.util.List;

import lombok.Data;

@Data
public class BulkProductCreateRequest {
  private List<ProductCreateRequest> products;
}
