
package com.streamshop.product_service.Mapper.imp;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import com.streamshop.product_service.Mapper.Mapper;
import com.streamshop.product_service.persistence.model.Product;
import com.streamshop.product_service.presentation.dto.ProductRequest;

import lombok.RequiredArgsConstructor;

/**
 * ProductMapper
 */

@Component
@RequiredArgsConstructor
public class ProductMapper implements Mapper<ProductRequest, Product> {
 
	private final ModelMapper modelMapper;

	@Override
	public ProductRequest mapToEntity(Product u) {
		return modelMapper.map(u, ProductRequest.class);
	}

	@Override
	public Product mapToDto(ProductRequest t) {
		return modelMapper.map(t, Product.class);
	}

}
