package com.streamshop.inventory_service.Expections;

import lombok.Getter;

@Getter
public class InsufficientInventoryException extends RuntimeException {
  public InsufficientInventoryException(String message) {
    super(message);
  }
}
