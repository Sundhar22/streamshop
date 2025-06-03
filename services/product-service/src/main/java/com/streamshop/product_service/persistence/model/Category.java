package com.streamshop.product_service.persistence.model;

import java.util.List;
import java.util.UUID;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.CompoundIndexes;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import lombok.Data;

@Document(collection = "categories")
@Data
@CompoundIndexes({
    @CompoundIndex(name = "name_parent", def = "{'name': 1, 'parent_id': 1}", unique = true)
})
public class Category {

  @Id
  private UUID id;

  @Field(name = "name")
  @Indexed(background = true)
  private String name;

  @Field(name = "parent_id")
  @Indexed(background = true)
  private UUID parentId;

  @Field(name = "path")
  private List<String> path;

  @Field(name = "product_ids")
  @Indexed(background = true)
  private List<UUID> productIds;

  public Category() {
    this.id = UUID.randomUUID();
  }
}
