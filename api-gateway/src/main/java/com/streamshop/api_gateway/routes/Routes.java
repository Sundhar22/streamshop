
package com.streamshop.api_gateway.routes;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.server.mvc.filter.CircuitBreakerFilterFunctions;
import org.springframework.cloud.gateway.server.mvc.handler.GatewayRouterFunctions;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.web.servlet.function.RequestPredicates;
import org.springframework.web.servlet.function.RouterFunction;
import org.springframework.web.servlet.function.ServerResponse;
import static org.springframework.cloud.gateway.server.mvc.filter.FilterFunctions.setPath;
import static org.springframework.cloud.gateway.server.mvc.filter.BeforeFilterFunctions.uri;
import static org.springframework.cloud.gateway.server.mvc.handler.HandlerFunctions.http;

import java.net.URI;

/**
 * Routes
 */

@Configuration
public class Routes {

  @Value("${product.service.baseurl}")
  private  String productServiceBaseurl ;
    @Value("${order.service.baseurl}")
    private  String orderServiceBaseurl ;
    @Value("${inventory.service.baseurl}")
    private  String inventoryServiceBaseurl ;


  @Bean
  RouterFunction<ServerResponse> productServiceRoutes() {
    return GatewayRouterFunctions.route("product-service")
        .route(RequestPredicates.path("/api/v1/products"),
            http())
        .before(uri(productServiceBaseurl))
        .route(RequestPredicates.path("/api/v1/categories"),
            http())
        .before(uri(productServiceBaseurl))

        .filter(CircuitBreakerFilterFunctions.circuitBreaker("product-service-circuit-breaker",
            URI.create("forward:/fallback")))
        .build();
  }

  @Bean
  RouterFunction<ServerResponse> orderServiceRoutes() {
    return GatewayRouterFunctions.route("order-service")
        .route(RequestPredicates.path("/api/v1/orders/**"),
            http())
        .before(uri(orderServiceBaseurl))
        .filter(CircuitBreakerFilterFunctions.circuitBreaker("order-service-circuit-breaker",
            URI.create("forward:/fallback")))

        .build();
  }

  @Bean
  RouterFunction<ServerResponse> inventoryServiceRoutes() {
    return GatewayRouterFunctions.route("inventory-service")
        .route(RequestPredicates.path("/api/v1/inventory/**"),
            http())
        .before(uri(inventoryServiceBaseurl))
        .filter(CircuitBreakerFilterFunctions.circuitBreaker("inventory-service-circuit-breaker",
            URI.create("forward:/fallback")))
        .build();
  }

  // Documentation routes can be added

  @Bean
  RouterFunction<ServerResponse> productServiceSwaggerRoute() {
    return GatewayRouterFunctions.route("product_service_swagger")
        .route(RequestPredicates.path("/aggregate/product-service/v3/api-docs"),
            http())
        .before(uri(productServiceBaseurl))
        .filter(setPath("/api-docs"))
        .filter(CircuitBreakerFilterFunctions.circuitBreaker("product-service-swagger-circuit-breaker",
            URI.create("forward:/fallback")))
        .build();
  }

  @Bean
  RouterFunction<ServerResponse> orderServiceSwaggerRoute() {
    return GatewayRouterFunctions.route("order_service_swagger")
        .route(RequestPredicates.path("/aggregate/order-service/v3/api-docs"),
            http())
        .before(uri(orderServiceBaseurl))
        .filter(setPath("/api-docs"))
        .filter(CircuitBreakerFilterFunctions.circuitBreaker("order-service-swagger-circuit-breaker",
            URI.create("forward:/fallback")))
        .build();
  }

  @Bean
  RouterFunction<ServerResponse> inventoryServiceSwaggerRoute() {
    return GatewayRouterFunctions.route("inventory_service_swagger")
        .route(RequestPredicates.path("/aggregate/inventory-service/v3/api-docs"),
            http())
        .before(uri(inventoryServiceBaseurl))
        .filter(setPath("/api-docs"))
        .filter(CircuitBreakerFilterFunctions.circuitBreaker("inventory-service-swagger-circuit-breaker",
            URI.create("forward:/fallback")))
        .build();
  }

  @Bean
  RouterFunction<ServerResponse> fallbackRoute() {
    return GatewayRouterFunctions.route("fallback")
        .GET("/fallback",
            request -> ServerResponse.status(HttpStatus.SERVICE_UNAVAILABLE)
                .body("Service is currently unavailable. Please try again later."))
        .build();
  }
}
