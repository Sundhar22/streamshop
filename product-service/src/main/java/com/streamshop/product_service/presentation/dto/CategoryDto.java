package com.streamshop.product_service.presentation.dto;

import lombok.Data;

import java.util.List;

@Data
public class CategoryDto {
  private String id;
  private String name;
  private String parentId;
  private List<String> path;
  private List<String> productIds;

}
