package com.streamshop.product_service.exception;

public class InvalidRequestException extends RuntimeException {

  public InvalidRequestException() {
    super("Invalid request.");
  }

  public InvalidRequestException(String message) {
    super(message);
  }

  public InvalidRequestException(String message, Throwable cause) {
    super(message, cause);
  }

  public InvalidRequestException(Throwable cause) {
    super(cause);
  }
}
