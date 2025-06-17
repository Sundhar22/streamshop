package com.streamshop.userservice.service;

import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.streamshop.userservice.dto.UserResponse;

@Service
public class KeycloakUserService {

  @Value("${keycloak.auth-server-url}")
  private String keycloakAuthUrl;

  @Value("${keycloak.realm}")
  private String realm;

  @Value("${keycloak.client-id}")
  private String clientId;

  @Value("${keycloak.client-secret}")
  private String clientSecret;

  private final RestTemplate restTemplate = new RestTemplate();

  public UserResponse getUserDetails(String userId) {
    String token = fetchAdminToken();

    HttpHeaders headers = new HttpHeaders();
    headers.setBearerAuth(token);
    headers.setContentType(MediaType.APPLICATION_JSON);

    HttpEntity<Void> request = new HttpEntity<>(headers);

    String url = String.format("%s/admin/realms/%s/users/%s", keycloakAuthUrl, realm, userId);
    ResponseEntity<Map> response = restTemplate.exchange(url, HttpMethod.GET, request, Map.class);

    Map<String, Object> userMap = response.getBody();

    UserResponse user = new UserResponse();
    user.setId((String) userMap.get("id"));
    user.setUsername((String) userMap.get("username"));
    user.setEmail((String) userMap.get("email"));
    user.setEnabled((Boolean) userMap.get("enabled"));
    return user;
  }

  private String fetchAdminToken() {
    String tokenUrl = String.format("%s/realms/%s/protocol/openid-connect/token", keycloakAuthUrl, realm);

    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

    String body = "grant_type=client_credentials" +
        "&client_id=" + clientId +
        "&client_secret=" + clientSecret;

    HttpEntity<String> entity = new HttpEntity<>(body, headers);

    ResponseEntity<Map> response = restTemplate.postForEntity(tokenUrl, entity, Map.class);
    return (String) response.getBody().get("access_token");
  }
}
