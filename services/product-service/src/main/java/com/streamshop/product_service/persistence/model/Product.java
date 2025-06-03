package com.streamshop.product_service.persistence.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Document(collection = "products")
@Data
@Setter
@Getter
public class Product {
  @Id
  @Indexed(unique = true)
  private UUID id;

  @Field(name = "name")
  private String name;

  @Field(name = "description")
  private String description;

  @Field(name = "price")
  private Double price;

  @Field(name = "sku")
  @Indexed(unique = true)
  private String sku;

  @Field(name = "category_ids")
  private List<UUID> categoryIds;

  @Field(name = "attributes")
  private Map<String, Object> attributes;

  // @Field(name = "stock_id")
  // private UUID stockId;

  @Field(name = "created_at")
  private LocalDateTime createdAt;

  @Field(name = "updated_at")
  private LocalDateTime updatedAt;

  public Product() {
    this.id = UUID.randomUUID();
  }
}
