package com.streamshop.product_service.service.imp;

import com.streamshop.product_service.exception.DuplicateResourceException;
import com.streamshop.product_service.exception.InvalidRequestException;
import com.streamshop.product_service.exception.ResourceNotFoundException;
import com.streamshop.product_service.persistence.model.Product;
import com.streamshop.product_service.persistence.repository.ProductRepository;
import com.streamshop.product_service.presentation.dto.BulkProductCreateRequest;
import com.streamshop.product_service.presentation.dto.PaginatedResponse;
import com.streamshop.product_service.presentation.dto.ProductCreateRequest;
import com.streamshop.product_service.presentation.dto.ProductDto;
import com.streamshop.product_service.presentation.dto.ProductSearchRequest;
import com.streamshop.product_service.presentation.dto.ProductUpdateRequest;
import com.streamshop.product_service.service.CategoryService;
import com.streamshop.product_service.service.ProductService;
import com.streamshop.product_service.utils.UuidUtils;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class ProductServiceImpl implements ProductService {

  private final ProductRepository productRepository;
  private final CategoryService categoryService;

  public ProductServiceImpl(ProductRepository productRepository, CategoryService categoryService) {
    this.productRepository = productRepository;
    this.categoryService = categoryService;
  }

  @Override
  public List<ProductDto> getAllProducts() {
    return productRepository.findAll().stream()
        .map(this::convertToDto)
        .collect(Collectors.toList());
  }

  @Override
  public Optional<ProductDto> getProductById(UUID id) {
    return productRepository.findById(id).map(this::convertToDto);
  }

  @Override
  public ProductDto createProduct(ProductCreateRequest request) {
    validateSkuUniqueness(request.getSku(), null);

    // Fail early if category does not exist
    validateCategoriesExist(request.getCategoryIds().stream()
        .map(UuidUtils::parseUuid)
        .collect(Collectors.toList()));

    Product product = convertToEntity(request);
    product.setCreatedAt(LocalDateTime.now());
    product.setUpdatedAt(LocalDateTime.now());

    Product saved = productRepository.save(product);
    updateCategoriesAfterChange(saved);

    return convertToDto(saved);
  }

  @Override
  public List<ProductDto> createBulkProducts(BulkProductCreateRequest bulkRequest) {
    return bulkRequest.getProducts().stream()
        .map(request -> {
          // Convert categoryIds from String to UUID
          List<UUID> categoryUuids = request.getCategoryIds().stream()
              .map(id -> {
                try {
                  return UuidUtils.parseUuid(id);
                } catch (IllegalArgumentException e) {
                  throw new InvalidRequestException("Invalid category ID: " + id);
                }
              })
              .toList();

          // Validate SKU uniqueness
          validateSkuUniqueness(request.getSku(), null);

          // Validate category existence
          validateCategoriesExist(categoryUuids);

          // Convert to entity and save
          Product product = convertToEntity(request);
          product.setCategoryIds(categoryUuids);
          product.setId(UUID.randomUUID());
          product.setCreatedAt(LocalDateTime.now());
          product.setUpdatedAt(LocalDateTime.now());

          return product;
        })
        .map(productRepository::save)
        .map(this::convertToDto)
        .toList();
  }

  @Override
  public ProductDto updateProduct(UUID id, ProductUpdateRequest request) {
    Product existing = productRepository.findById(id).orElseThrow();

    validateCategoriesExist(request.getCategoryIds().stream()
        .map(UuidUtils::parseUuid)
        .collect(Collectors.toList()));

    existing.setName(request.getName());
    existing.setDescription(request.getDescription());
    existing.setPrice(request.getPrice());
    existing.setSku(request.getSku());

    // Convert List<String> -> List<UUID> for categoryIds
    existing.setCategoryIds(request.getCategoryIds() != null
        ? UuidUtils.parseUuids(request.getCategoryIds())
        : null);

    existing.setAttributes(request.getAttributes());
    existing.setUpdatedAt(LocalDateTime.now());

    Product updated = productRepository.save(existing);

    updateCategoriesAfterChange(updated);

    return convertToDto(updated);
  }

  @Override
  public void deleteProduct(UUID id) {
    Product product = productRepository.findById(id).orElseThrow();
    removeProductFromCategories(product);
    productRepository.deleteById(id);
  }

  @Override
  public List<ProductDto> searchProducts(ProductSearchRequest searchRequest) {
    if (searchRequest.getCategory() == null && searchRequest.getMinPrice() == null
        && searchRequest.getMaxPrice() == null) {
      return getAllProducts();
    }

    return productRepository.findAll().stream()
        .filter(p -> searchRequest.getCategory() == null || p.getCategoryIds().contains(searchRequest.getCategory()))
        .filter(p -> searchRequest.getMinPrice() == null || p.getPrice() >= searchRequest.getMinPrice())
        .filter(p -> searchRequest.getMaxPrice() == null || p.getPrice() <= searchRequest.getMaxPrice())
        .map(this::convertToDto)
        .collect(Collectors.toList());
  }

  @Override
  public PaginatedResponse<ProductDto> getAllProducts(int page, int size) {

    Page<Product> productPage = productRepository.findAll(PageRequest.of(page, size));

    List<ProductDto> productDtos = productPage.getContent().stream()
        .map(this::convertToDto)
        .toList();
    return PaginatedResponse.of(productDtos, page + 1, size, productPage.getTotalElements());
  }

  @Override
  public ProductDto updateProductStockId(UUID productId, String stockId) {
    Product existing = productRepository.findById(productId)
        .orElseThrow(() -> new ResourceNotFoundException("Product", productId));

    existing.setUpdatedAt(LocalDateTime.now());
    Product updated = productRepository.save(existing);
    return convertToDto(updated);
  }

  // Helper methods
  private ProductDto convertToDto(Product product) {
    ProductDto dto = new ProductDto();
    dto.setId(product.getId().toString()); // UUID -> String
    dto.setName(product.getName());
    dto.setDescription(product.getDescription());
    dto.setPrice(product.getPrice());
    dto.setSku(product.getSku());
    dto.setCategoryIds(
        product.getCategoryIds() != null
            ? product.getCategoryIds().stream()
                .map(UUID::toString)
                .toList()
            : null);
    dto.setAttributes(product.getAttributes());
    dto.setCreatedAt(product.getCreatedAt());
    dto.setUpdatedAt(product.getUpdatedAt());
    return dto;
  }

  private Product convertToEntity(ProductCreateRequest request) {
    Product product = new Product();
    product.setId(UUID.randomUUID());
    product.setName(request.getName());
    product.setDescription(request.getDescription());
    product.setPrice(request.getPrice());
    product.setSku(request.getSku());

    if (request.getCategoryIds() != null && !request.getCategoryIds().isEmpty()) {
      product.setCategoryIds(UuidUtils.parseUuids(request.getCategoryIds()));
    }
    product.setAttributes(request.getAttributes());
    return product;
  }

  private void updateCategoriesAfterChange(Product product) {
    if (product.getCategoryIds() != null) {
      product.getCategoryIds().forEach(categoryId -> {
        categoryService.addProductToCategory(categoryId, product.getId());
      });
    }
  }

  private void removeProductFromCategories(Product product) {
    if (product.getCategoryIds() != null) {
      product.getCategoryIds().forEach(categoryId -> {
        categoryService.removeProductFromCategory(categoryId, product.getId());
      });
    }
  }

  // validation methods
  private void validateSkuUniqueness(String sku, UUID existingId) {
    Product existing = productRepository.findBySku(sku);
    if (existing != null && (existingId == null || !existing.getId().equals(existingId))) {
      throw new DuplicateResourceException("Product with SKU [" + sku + "] already exists.");
    }
  }

  private void validateCategoriesExist(List<UUID> categoryIds) {
    if (categoryIds != null) {
      for (UUID categoryId : categoryIds) {
        if (!categoryService.existsById(categoryId)) {
          throw new ResourceNotFoundException("Category", categoryId);
        }
      }
    }
  }
}
