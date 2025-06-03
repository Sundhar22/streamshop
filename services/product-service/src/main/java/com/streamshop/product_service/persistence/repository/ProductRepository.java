package com.streamshop.product_service.persistence.repository;

import com.streamshop.product_service.persistence.model.Product;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ProductRepository extends MongoRepository<Product, UUID> {
  List<Product> findByCategoryIdsContaining(UUID categoryId);

  Product findBySku(String sku);
}
