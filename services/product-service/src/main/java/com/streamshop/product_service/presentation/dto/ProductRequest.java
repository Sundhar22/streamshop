
package com.streamshop.product_service.presentation.dto;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * ProductRequest
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductRequest {
  private String name;
  private String description;
  private BigDecimal price;
  private List<String> categoryIds;
  private Map<String, Object> attributes;
}
