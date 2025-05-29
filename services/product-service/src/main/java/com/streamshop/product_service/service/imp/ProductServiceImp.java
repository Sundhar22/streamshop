
package com.streamshop.product_service.service.imp;

import java.util.List;

import org.springframework.stereotype.Service;

import com.streamshop.product_service.Mapper.imp.ProductMapper;
import com.streamshop.product_service.persistence.model.Product;
import com.streamshop.product_service.persistence.repository.ProductRepository;
import com.streamshop.product_service.presentation.dto.ProductRequest;
import com.streamshop.product_service.service.ProductService;

import lombok.RequiredArgsConstructor;

/**
 * ProductServiceImp
 */

@Service
@RequiredArgsConstructor
public class ProductServiceImp implements ProductService {

  private final ProductRepository productRepository;
  private final ProductMapper productMapper;

  @Override
  public Product addProduct(ProductRequest productRequest) {
    final Product newProduct = productMapper.mapToDto(productRequest);

    return productRepository.save(newProduct);
  }

  @Override
  public List<Product> getAllProducts() {
    return productRepository.findAll();
  }

}
