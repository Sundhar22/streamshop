package com.streamshop.product_service.persistence.repository;

import java.util.UUID;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.streamshop.product_service.persistence.model.Category;

@Repository
public interface CategoryRepository extends MongoRepository<Category, UUID> {
  boolean existsByNameAndParentId(String name, UUID parentId);
}
