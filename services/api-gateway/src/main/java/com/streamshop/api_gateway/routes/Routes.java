
package com.streamshop.api_gateway.routes;

import org.springframework.cloud.gateway.server.mvc.handler.GatewayRouterFunctions;
import org.springframework.cloud.gateway.server.mvc.handler.HandlerFunctions;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.function.RequestPredicates;
import org.springframework.web.servlet.function.RouterFunction;
import org.springframework.web.servlet.function.ServerResponse;

/**
 * Routes
 */
@Configuration
public class Routes {

  @Bean
  RouterFunction<ServerResponse> productServiceRoutes() {
    return GatewayRouterFunctions.route("product-service")
        .route(RequestPredicates.path("/api/v1/products"), HandlerFunctions.http("http://localhost:8080"))
        .route(RequestPredicates.path("/api/v1/categories"), HandlerFunctions.http("http://localhost:8080"))
        .build();
  }

  @Bean
  RouterFunction<ServerResponse> orderServiceRoutes() {
    return GatewayRouterFunctions.route("order-service")
        .route(RequestPredicates.path("/api/v1/orders"), HandlerFunctions.http("http://localhost:8081"))
        .build();
  }

  @Bean
  RouterFunction<ServerResponse> inventoryServiceRoutes() {
    return GatewayRouterFunctions.route("inventory-service")
        .route(RequestPredicates.path("/api/v1/inventory"), HandlerFunctions.http("http://localhost:8082"))
        .build();
  }

}
