package com.streamshop.order.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.OpenAPI;

/**
 * OpenAPIConfig
 */
@Configuration
public class OpenAPIConfig {

  @Bean
  OpenAPI productServiceOpenAPI() {
    return new OpenAPI()
        .info(new io.swagger.v3.oas.models.info.Info()
            .title("Order Service API")
            .description("API for managing orders in the StreamShop application")
            .version("1.0.0").license(
                new io.swagger.v3.oas.models.info.License()
                    .name("Apache 2.0")
                    .url("http://www.apache.org/licenses/LICENSE-2.0.html")));
  }

}
