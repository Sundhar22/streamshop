package com.streamshop.product_service.presentation.dto;

import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class ProductCreateRequest {
  private String name;
  private String description;
  private Double price;
  private String sku;
  private List<String> categoryIds;
  private Map<String, Object> attributes;
}
