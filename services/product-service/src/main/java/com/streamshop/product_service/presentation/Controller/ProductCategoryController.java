package com.streamshop.product_service.presentation.Controller;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.streamshop.product_service.presentation.dto.BulkProductCreateRequest;
import com.streamshop.product_service.presentation.dto.CategoryDto;
import com.streamshop.product_service.presentation.dto.PaginatedResponse;
import com.streamshop.product_service.presentation.dto.ProductCreateRequest;
import com.streamshop.product_service.presentation.dto.ProductDto;
import com.streamshop.product_service.presentation.dto.ProductUpdateRequest;
import com.streamshop.product_service.service.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1")
public class ProductCategoryController {

  @Autowired
  private ProductService productService;

  @Autowired
  private CategoryService categoryService;

  // ============= Product Endpoints =============

  /**
   * GET /products - Get all products
   */
  // @GetMapping("/products")
  // public ResponseEntity<List<ProductDto>> getAllProducts(
  // @RequestParam(required = false) UUID category,
  // @RequestParam(required = false) Double minPrice,
  // @RequestParam(required = false) Double maxPrice) {
  //
  // ProductSearchRequest searchRequest = new ProductSearchRequest();
  //
  // searchRequest.setCategory(category);
  // searchRequest.setMinPrice(minPrice);
  // searchRequest.setMaxPrice(maxPrice);
  //
  // List<ProductDto> products = productService.searchProducts(searchRequest);
  // return ResponseEntity.ok(products);
  // }

  @GetMapping("/products")
  public ResponseEntity<PaginatedResponse<ProductDto>> getAllProducts(
      @RequestParam(defaultValue = "0") int page,
      @RequestParam(defaultValue = "10") int size) {

    int zeroBasedPage = Math.max(0, page - 1);
    PaginatedResponse<ProductDto> products = productService.getAllProducts(zeroBasedPage, size);
    return ResponseEntity.ok(products);
  }

  /**
   * GET /products/{id} - Get product by ID
   */
  @GetMapping("/products/{id}")
  public ResponseEntity<ProductDto> getProductById(@PathVariable UUID id) {
    Optional<ProductDto> product = productService.getProductById(id);
    return product.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
  }

  /**
   * POST /products - Create a new product
   */
  @PostMapping("/products")
  public ResponseEntity<ProductDto> createProduct(@RequestBody ProductCreateRequest request) {
    ProductDto createdProduct = productService.createProduct(request);
    return ResponseEntity.status(HttpStatus.CREATED).body(createdProduct);
  }

  @PostMapping("/products/bulk")
  public ResponseEntity<List<ProductDto>> createBulkProducts(@RequestBody BulkProductCreateRequest bulkRequest) {
    List<ProductDto> created = productService.createBulkProducts(bulkRequest);
    return ResponseEntity.status(HttpStatus.CREATED).body(created);
  }

  /**
   * PUT /products/{id} - Update an existing product
   */
  @PutMapping("/products/{id}")
  public ResponseEntity<ProductDto> updateProduct(
      @PathVariable UUID id,
      @RequestBody ProductUpdateRequest request) {
    ProductDto updatedProduct = productService.updateProduct(id, request);
    return ResponseEntity.ok(updatedProduct);
  }

  /**
   * PUT /products/{id}/stock - Update stock ID (used by Inventory Service)
   */
  @PutMapping("/products/{id}/stock")
  public ResponseEntity<ProductDto> updateProductStockId(
      @PathVariable UUID id,
      @RequestBody Map<String, String> payload) {

    String stockId = payload.get("stockId");
    ProductDto updated = productService.updateProductStockId(id, stockId);
    return ResponseEntity.ok(updated);
  }

  /**
   * DELETE /products/{id} - Delete a product
   */
  @DeleteMapping("/products/{id}")
  public ResponseEntity<Void> deleteProduct(@PathVariable UUID id) {
    productService.deleteProduct(id);
    return ResponseEntity.noContent().build();
  }

  // ============= Category Endpoints =============

  /**
   * GET /categories - Get all categories
   */
  // @GetMapping("/categories")
  // public ResponseEntity<List<CategoryDto>> getAllCategories() {
  // List<CategoryDto> categories = categoryService.getAllCategories();
  // return ResponseEntity.ok(categories);
  // }

  @GetMapping("/categories")
  public ResponseEntity<PaginatedResponse<CategoryDto>> getAllCategories(
      @RequestParam(defaultValue = "0") int page,
      @RequestParam(defaultValue = "10") int size) {
    int zeroBasedPage = Math.max(0, page - 1);

    PaginatedResponse<CategoryDto> categories = categoryService.getAllCategories(zeroBasedPage, size);
    return ResponseEntity.ok(categories);
  }

  /**
   * GET /categories/{id} - Get category by ID
   */
  @GetMapping("/categories/{id}")
  public ResponseEntity<CategoryDto> getCategoryById(@PathVariable UUID id) {
    Optional<CategoryDto> category = categoryService.getCategoryById(id);
    return category.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
  }

  /**
   * POST /categories - Create a new category
   */
  @PostMapping("/categories")
  public ResponseEntity<CategoryDto> createCategory(@RequestBody CategoryDto categoryDto) {
    CategoryDto createdCategory = categoryService.createCategory(categoryDto);
    return ResponseEntity.status(HttpStatus.CREATED).body(createdCategory);
  }

  /**
   * PUT /categories/{id} - Update an existing category
   */
  @PutMapping("/categories/{id}")
  public ResponseEntity<CategoryDto> updateCategory(
      @PathVariable UUID id,
      @RequestBody CategoryDto categoryDto) {
    CategoryDto updatedCategory = categoryService.updateCategory(id, categoryDto);
    return ResponseEntity.ok(updatedCategory);
  }

  /**
   * DELETE /categories/{id} - Delete a category
   */
  @DeleteMapping("/categories/{id}")
  public ResponseEntity<Void> deleteCategory(@PathVariable UUID id) {
    categoryService.deleteCategory(id);
    return ResponseEntity.noContent().build();
  }

  // ============= Extra: Manage Products in Categories =============

  /**
   * POST /categories/{categoryId}/products/{productId} - Add product to category
   */
  @PostMapping("/categories/{categoryId}/products/{productId}")
  public ResponseEntity<Void> addProductToCategory(
      @PathVariable UUID categoryId,
      @PathVariable UUID productId) {
    categoryService.addProductToCategory(categoryId, productId);
    return ResponseEntity.ok().build();
  }

  /**
   * DELETE /categories/{categoryId}/products/{productId} - Remove product from
   * category
   */
  @DeleteMapping("/categories/{categoryId}/products/{productId}")
  public ResponseEntity<Void> removeProductFromCategory(
      @PathVariable UUID categoryId,
      @PathVariable UUID productId) {
    categoryService.removeProductFromCategory(categoryId, productId);
    return ResponseEntity.ok().build();
  }

}
