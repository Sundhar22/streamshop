package com.streamshop.userservice.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.streamshop.userservice.dto.UserResponse;
import com.streamshop.userservice.service.KeycloakUserService;

@RestController
@RequestMapping("/users")
public class UserController {

  private final KeycloakUserService userService;

  public UserController(KeycloakUserService userService) {
    this.userService = userService;
  };

  @GetMapping("/{userId}")
  public UserResponse getUser(@PathVariable String userId) {
    return userService.getUserDetails(userId);
  }
}
