package com.streamshop.product_service.service.imp;

import com.streamshop.product_service.exception.DuplicateResourceException;
import com.streamshop.product_service.exception.ResourceNotFoundException;
import com.streamshop.product_service.persistence.model.Category;
import com.streamshop.product_service.persistence.repository.CategoryRepository;
import com.streamshop.product_service.presentation.dto.CategoryDto;
import com.streamshop.product_service.presentation.dto.PaginatedResponse;
import com.streamshop.product_service.service.CategoryService;
import com.streamshop.product_service.utils.UuidUtils;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class CategoryServiceImpl implements CategoryService {

  private final CategoryRepository categoryRepository;

  public CategoryServiceImpl(CategoryRepository categoryRepository) {
    this.categoryRepository = categoryRepository;
  }

  @Override
  public List<CategoryDto> getAllCategories() {
    return categoryRepository.findAll().stream()
        .map(this::convertToDto)
        .collect(Collectors.toList());
  }

  @Override
  public PaginatedResponse<CategoryDto> getAllCategories(int page, int size) {
    System.out.println(
        categoryRepository.findAll().stream()
            .map(this::convertToDto)
            .collect(Collectors.toList()));

    Page<Category> categoryPage = categoryRepository.findAll(PageRequest.of(page, size));

    List<CategoryDto> categoryDtos = categoryPage.getContent().stream()
        .map(this::convertToDto)
        .toList();
    return PaginatedResponse.of(categoryDtos, page + 1, size, categoryPage.getTotalElements());
  }

  @Override
  public Optional<CategoryDto> getCategoryById(UUID id) {
    return categoryRepository.findById(id).map(this::convertToDto);
  }

  @Override
  public CategoryDto createCategory(CategoryDto dto) {
    // Convert parentId from String to UUID using UuidUtils
    UUID parentId = UuidUtils.parseUuid(dto.getParentId());

    // Check for duplicates
    if (dto.getName() != null && categoryRepository.existsByNameAndParentId(dto.getName(), parentId)) {
      throw new DuplicateResourceException("Category with name '" + dto.getName() +
          "' under the same parent already exists.");
    }

    // Convert DTO to entity
    Category category = convertToEntity(dto);

    // Ensure ID is generated if not provided
    if (category.getId() == null) {
      category.setId(UUID.randomUUID());
    }

    category.setParentId(parentId); // Ensure parentId is properly set

    Category saved = categoryRepository.save(category);
    return convertToDto(saved);
  }

  @Override
  public CategoryDto updateCategory(UUID id, CategoryDto dto) {
    // 1. Fetch existing category or throw not found
    Category existing = categoryRepository.findById(id)
        .orElseThrow(() -> new ResourceNotFoundException("Category", id));

    // 2. Parse parentId safely using UuidUtils
    UUID parentId = UuidUtils.parseUuid(dto.getParentId());

    // 3. Only check for duplicates if name or parentId has changed
    boolean nameChanged = !existing.getName().equals(dto.getName());
    boolean parentIdChanged = !Objects.equals(existing.getParentId(), parentId);

    if (nameChanged || parentIdChanged) {
      if (categoryRepository.existsByNameAndParentId(dto.getName(), parentId)) {
        throw new DuplicateResourceException("Category with name '" + dto.getName() +
            "' under this parent already exists.");
      }
    }

    // 4. Update fields
    existing.setName(dto.getName());
    existing.setParentId(parentId);
    existing.setPath(dto.getPath());

    if (dto.getProductIds() != null) {
      existing.setProductIds(
          dto.getProductIds().stream()
              .map(UuidUtils::parseUuid)
              .toList());
    }

    // 5. Save and return as DTO
    Category updated = categoryRepository.save(existing);
    return convertToDto(updated);
  }

  @Override
  public void deleteCategory(UUID id) {
    categoryRepository.deleteById(id);
  }

  @Override
  public void addProductToCategory(UUID categoryId, UUID productId) {
    Optional<Category> optionalCategory = categoryRepository.findById(categoryId);
    if (optionalCategory.isPresent()) {
      Category category = optionalCategory.get();
      if (category.getProductIds() == null) {
        category.setProductIds(Collections.singletonList(productId));
      } else if (!category.getProductIds().contains(productId)) {
        category.getProductIds().add(productId);
      }
      categoryRepository.save(category);
    } else {
      throw new ResourceNotFoundException("Category", categoryId);
    }
  }

  @Override
  public void removeProductFromCategory(UUID categoryId, UUID productId) {
    Category category = categoryRepository.findById(categoryId).orElseThrow();
    if (category.getProductIds() != null) {
      category.getProductIds().remove(productId);
      categoryRepository.save(category);
    }
  }

  private CategoryDto convertToDto(Category category) {

    CategoryDto dto = new CategoryDto();
    dto.setId(category.getId().toString());
    dto.setName(category.getName());

    System.out.println(category.getId().toString());
    if (category.getParentId() != null) {
      dto.setParentId(category.getParentId().toString()); // UUID -> String
    }

    dto.setPath(category.getPath());

    if (category.getProductIds() != null) {
      dto.setProductIds(
          category.getProductIds().stream()
              .map(UUID::toString)
              .toList());
    } else {
      dto.setProductIds(Collections.emptyList());
    }

    return dto;
  }

  private Category convertToEntity(CategoryDto dto) {
    Category category = new Category();
    category.setId(dto.getId() != null ? UuidUtils.parseUuid(dto.getId()) : null);
    category.setName(dto.getName());
    category.setParentId(UuidUtils.parseUuid(dto.getParentId())); // Safe parsing
    category.setPath(dto.getPath());

    if (dto.getProductIds() != null) {
      category.setProductIds(
          dto.getProductIds().stream()
              .map(UuidUtils::parseUuid)
              .toList());
    }

    return category;
  }

  @Override
  public boolean existsById(UUID id) {
    return categoryRepository.existsById(id);
  }
}
