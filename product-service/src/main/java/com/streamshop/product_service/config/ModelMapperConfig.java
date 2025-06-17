package com.streamshop.product_service.config;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.modelmapper.convention.MatchingStrategies;

@Configuration
public class ModelMapperConfig {
  @Bean
  ModelMapper modelMapper() {
    ModelMapper modelMapper = new ModelMapper();
    modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.LOOSE);
    return modelMapper;
  }
}
