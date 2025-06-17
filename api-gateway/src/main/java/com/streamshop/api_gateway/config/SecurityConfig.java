package com.streamshop.api_gateway.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;

import java.util.*;
import java.util.stream.Collectors;

/**
 * SecurityConfig
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {

  private final String[] freeResourceUrls = { "/swagger-ui.html", "/swagger-ui/**", "/v3/api-docs/**",
      "/swagger-resources/**", "/api-docs/**", "/aggregate/**", "/actuator/prometheus" };

  @Bean
  SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

    return http
        .csrf(csrf -> csrf.disable())
        .authorizeHttpRequests(authorize -> authorize
            // Public endpoints (no authentication required)
            .requestMatchers(HttpMethod.GET, "/api/v1/products/**").permitAll()
            .requestMatchers(HttpMethod.GET, "/api/v1/categories/**").permitAll()
            .requestMatchers(HttpMethod.POST, "/api/v1/inventory/check-availability").permitAll()
            .requestMatchers(freeResourceUrls).permitAll()

            // User endpoints (authenticated users)
            .requestMatchers(HttpMethod.POST, "/api/v1/orders").authenticated()
            .requestMatchers(HttpMethod.GET, "/api/v1/orders/{orderId}").authenticated()
            .requestMatchers(HttpMethod.GET, "/api/v1/orders/user/{userId}").authenticated()
            .requestMatchers(HttpMethod.POST, "/api/v1/orders/{orderId}/cancel").authenticated()
            .requestMatchers(HttpMethod.POST, "/api/v1/orders/{orderId}/return").authenticated()

            // Admin endpoints
            .requestMatchers(HttpMethod.PUT, "/api/v1/orders/{orderId}/status").hasAuthority("ROLE_ADMIN")

            .requestMatchers(HttpMethod.POST, "/api/v1/products/**").hasAuthority("ROLE_ADMIN")

            .requestMatchers(HttpMethod.PUT, "/api/v1/products/**").hasAuthority("ROLE_ADMIN")

            .requestMatchers(HttpMethod.DELETE, "/api/v1/products/**").hasAuthority("ROLE_ADMIN")

            .requestMatchers(HttpMethod.POST, "/api/v1/categories/**").hasAuthority("ROLE_ADMIN")

            .requestMatchers(HttpMethod.PUT, "/api/v1/categories/**").hasAuthority("ROLE_ADMIN")

            .requestMatchers(HttpMethod.DELETE, "/api/v1/categories/**").hasAuthority("ROLE_ADMIN")

            .requestMatchers(HttpMethod.POST, "/api/v1/inventory/**").hasAuthority("ROLE_ADMIN")
            .requestMatchers(HttpMethod.GET, "/api/v1/inventory/**").hasAuthority("ROLE_ADMIN")

            // Any other request requires authentication
            .anyRequest().authenticated())
        .oauth2ResourceServer(oauth2 -> oauth2
            .jwt(jwt -> jwt.jwtAuthenticationConverter(jwtAuthenticationConverter())))
        .build();

  }

  @Bean
  JwtAuthenticationConverter jwtAuthenticationConverter() {
    JwtAuthenticationConverter converter = new JwtAuthenticationConverter();

    converter.setJwtGrantedAuthoritiesConverter(jwt -> {
      Map<String, Object> realmAccess = jwt.getClaim("realm_access");
      if (realmAccess == null || !realmAccess.containsKey("roles")) {
        return Collections.emptyList();
      }

      @SuppressWarnings("unchecked")
      List<String> roles = (List<String>) realmAccess.get("roles");

      return roles.stream()
              // prefix each realm role with "ROLE_"
              .map(role -> new SimpleGrantedAuthority("ROLE_" + role))
              .collect(Collectors.toList());
    });

    return converter;
  }
}
