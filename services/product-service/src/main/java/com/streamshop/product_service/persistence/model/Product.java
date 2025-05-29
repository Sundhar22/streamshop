
package com.streamshop.product_service.persistence.model;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.Map;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Product
 */
@Document(value = "product")
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class Product {
  @Id
  private String id;
  private String name;
  private String description;
  private BigDecimal price;
  private List<String> categoryIds;
  private Map<String, Object> attributes;
  private Instant createdAt;
  private Instant updatedAt;
}
