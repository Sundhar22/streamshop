
package com.streamshop.product_service.Mapper;

/**
 * Mapper
 */
public interface Mapper<T, U> {
  T mapToEntity(U u);

  U mapToDto(T t);

}
