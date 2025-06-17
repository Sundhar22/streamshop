package com.streamshop.product_service.service;

import com.streamshop.product_service.presentation.dto.BulkProductCreateRequest;
import com.streamshop.product_service.presentation.dto.PaginatedResponse;
import com.streamshop.product_service.presentation.dto.ProductCreateRequest;
import com.streamshop.product_service.presentation.dto.ProductDto;
import com.streamshop.product_service.presentation.dto.ProductSearchRequest;
import com.streamshop.product_service.presentation.dto.ProductUpdateRequest;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ProductService {

  List<ProductDto> getAllProducts();

  Optional<ProductDto> getProductById(UUID id);

  ProductDto createProduct(ProductCreateRequest request);

  ProductDto updateProduct(UUID id, ProductUpdateRequest request);

  void deleteProduct(UUID id);

  List<ProductDto> searchProducts(ProductSearchRequest searchRequest);

  PaginatedResponse<ProductDto> getAllProducts(int page, int size);

  ProductDto updateProductStockId(UUID productId, String stockId);

  List<ProductDto> createBulkProducts(BulkProductCreateRequest bulkRequest);
}
