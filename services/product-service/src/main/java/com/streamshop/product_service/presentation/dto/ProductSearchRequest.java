package com.streamshop.product_service.presentation.dto;

import java.util.UUID;

import lombok.Data;

@Data
public class ProductSearchRequest {
  private UUID category;
  private Double minPrice;
  private Double maxPrice;
}
