package com.streamshop.product_service.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ProductControllerAdvice {

  @ExceptionHandler(DuplicateResourceException.class)
  public ResponseEntity<String> handleDuplicateResource() {
    return ResponseEntity.status(HttpStatus.CONFLICT).body("Resource already exists.");
  }

  @ExceptionHandler(ResourceNotFoundException.class)
  public ResponseEntity<String> handleResourceNotFound() {
    return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Resource not found.");
  }
}
