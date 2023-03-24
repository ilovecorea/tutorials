package org.example.petclinic;

import static io.restassured.RestAssured.when;

import io.restassured.RestAssured;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.boot.test.context.SpringBootTest;

@Slf4j
@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class TestPetclinicVerticle {

  @BeforeAll
  public void init() {
    RestAssured.port = 8080;
  }

  @Test
  public void testListPetType() {
    when()
        .get("/pettypes")
        .then()
        .assertThat()
        .statusCode(200)
        .log().all();
  }
}
