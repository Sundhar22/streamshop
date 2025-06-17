package com.streamshop.product_service.exception;

public class ResourceNotFoundException extends RuntimeException {

  public ResourceNotFoundException(String resourceType, String id) {
    super(resourceType + " with ID [" + id + "] not found.");
  }

  public ResourceNotFoundException(String resourceType, java.util.UUID id) {
    this(resourceType, id.toString());
  }
}
