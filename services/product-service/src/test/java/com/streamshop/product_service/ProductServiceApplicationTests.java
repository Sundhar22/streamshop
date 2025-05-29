package com.streamshop.product_service;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.context.annotation.Import;
import org.testcontainers.containers.MongoDBContainer;

import io.restassured.RestAssured;

@Import(TestcontainersConfiguration.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ProductServiceApplicationTests {

  @ServiceConnection
  static MongoDBContainer mongoDbContainer = new MongoDBContainer("mongo:7.0.5");

  @LocalServerPort
  private Integer port;

  @BeforeEach
  void setUp() {
    RestAssured.baseURI = "http://localhost";
    RestAssured.port = port;
  }

  static {
    mongoDbContainer.start();
  }

  @Test
  void contextLoads() {
    String resposeBody = """
            {
                "name": "iPhone 15",
                "description": "iPhone 15 is a smartphone from Apple",
                "price": 1008
            }
        """;

    RestAssured.given()
        .contentType("application/json")
        .body(resposeBody)
        .when()
        .post("/api/product")
        .then()
        .statusCode(201)
        .body("id", Matchers.notNullValue())
        .body("name", Matchers.equalTo("iPhone 15"))
        .body("description", Matchers.equalTo("iPhone 15 is a smartphone from Apple"))
        .body("price", Matchers.equalTo(1008));
  }

}
