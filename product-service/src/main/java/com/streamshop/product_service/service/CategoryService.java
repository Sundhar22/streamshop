package com.streamshop.product_service.service;

import com.streamshop.product_service.presentation.dto.CategoryDto;
import com.streamshop.product_service.presentation.dto.PaginatedResponse;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CategoryService {

  List<CategoryDto> getAllCategories();

  Optional<CategoryDto> getCategoryById(UUID id);

  CategoryDto createCategory(CategoryDto dto);

  CategoryDto updateCategory(UUID id, CategoryDto dto);

  void deleteCategory(UUID id);

  void addProductToCategory(UUID categoryId, UUID productId);

  void removeProductFromCategory(UUID categoryId, UUID productId);

  boolean existsById(UUID id);

  PaginatedResponse<CategoryDto> getAllCategories(int page, int size);
}
