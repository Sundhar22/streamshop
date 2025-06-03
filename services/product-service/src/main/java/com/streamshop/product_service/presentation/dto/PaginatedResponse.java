package com.streamshop.product_service.presentation.dto;

import java.util.List;

import lombok.Data;

@Data
public class PaginatedResponse<T> {
  private List<T> content;
  private int page;
  private int size;
  private long totalElements;
  private int totalPages;

  public static <T> PaginatedResponse<T> of(List<T> content, int page, int size, long totalElements) {
    PaginatedResponse<T> response = new PaginatedResponse<>();
    response.setContent(content);
    response.setPage(page);
    response.setSize(size);
    response.setTotalElements(totalElements);
    response.setTotalPages((int) Math.ceil((double) totalElements / size));
    return response;
  }
}
