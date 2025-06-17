package com.streamshop.order.Client;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.streamshop.order.presentation.dto.FeignDto.UserResponse;

@Component
public class UserServiceClient {

  @Autowired
  private final RestTemplate restTemplate;

  @Value("${base-url}")
  private String userServiceBaseUrl;

  public UserServiceClient(RestTemplate restTemplate) {
    this.restTemplate = restTemplate;
  }

  public UserResponse getUserById(String userId) {
    String url = userServiceBaseUrl + "/users/" + userId;
    ResponseEntity<UserResponse> response = restTemplate.exchange(
        url,
        HttpMethod.GET,
        new HttpEntity<>(new HttpHeaders()),
        UserResponse.class);
    return response.getBody();
  }
}
