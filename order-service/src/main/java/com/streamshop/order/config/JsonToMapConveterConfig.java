
package com.streamshop.order.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.fasterxml.jackson.databind.ObjectMapper;

@Configuration
public class JsonToMapConveterConfig {

  @Bean
  ObjectMapper jsonToMapConverter() {
    return new ObjectMapper();
  }

}
