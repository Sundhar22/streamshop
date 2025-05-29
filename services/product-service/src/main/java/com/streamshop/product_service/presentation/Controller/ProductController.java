package com.streamshop.product_service.presentation.Controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.streamshop.product_service.Mapper.imp.ProductMapper;
import com.streamshop.product_service.persistence.model.Product;
import com.streamshop.product_service.presentation.dto.ProductRequest;
import com.streamshop.product_service.service.ProductService;

import lombok.AllArgsConstructor;

import java.util.List;

import org.springframework.http.HttpStatus;

/**
 * ProductController
 */

@RestController
@RequestMapping("/api/v1/product")
@AllArgsConstructor
public class ProductController {
  private final ProductService productService;
  private final ProductMapper mapper;

  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  public ProductRequest createProduct(@RequestBody ProductRequest productRequest) {
    return mapper.mapToEntity(productService.addProduct(productRequest));
  }

  @GetMapping
  @ResponseStatus(HttpStatus.OK)
  public List<ProductRequest> getAllProduct() {
    List<Product> productList = productService.getAllProducts();
    return productList.stream().map((product) -> mapper.mapToEntity(product)).toList();
  }

}
