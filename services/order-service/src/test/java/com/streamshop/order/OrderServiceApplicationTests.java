package com.streamshop.order;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.context.annotation.Import;
import org.testcontainers.containers.MySQLContainer;

@Import(TestcontainersConfiguration.class)
@SpringBootTest
class OrderServiceApplicationTests {
  @SuppressWarnings("rawtypes")
  @ServiceConnection
  static MySQLContainer mySqlContainer = new MySQLContainer<>("mysql:8.3.0");

  @LocalServerPort
  private Integer port;

  static {
    mySqlContainer.start();
  }

  @BeforeEach
  void setUp() {
  }

  @Test
  void contextLoads() {
  }

}
