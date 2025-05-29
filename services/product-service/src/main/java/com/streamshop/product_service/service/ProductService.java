package com.streamshop.product_service.service;

import java.util.List;

import com.streamshop.product_service.persistence.model.Product;
import com.streamshop.product_service.presentation.dto.ProductRequest;

/**
 * ProductService
 */
public interface ProductService {

  public Product addProduct(ProductRequest productRequest);

  public List<Product> getAllProducts();

}
