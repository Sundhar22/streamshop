
package com.streamshop.product_service.persistence.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.streamshop.product_service.persistence.model.Product;

/**
 * ProductRepository
 */
public interface ProductRepository extends MongoRepository<Product, String> {
}

