package com.example.petclinic.repository;

import static io.restassured.RestAssured.given;
import static io.restassured.RestAssured.when;
import static org.hamcrest.Matchers.is;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.example.petclinic.PetclinicDataRestApplication;
import org.example.petclinic.model.PetType;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;

@SpringBootTest(classes = PetclinicDataRestApplication.class, webEnvironment = WebEnvironment.DEFINED_PORT)
public class VetRepositoryLiveTest {

  @BeforeAll
  public static void init() {
    RestAssured.baseURI = "http://localhost:8080";
    RestAssured.port = 8080;
  }

  @Test
  public void testFindAllOk() {
    when()
        .get("/vets")
        .then()
        .assertThat()
        .statusCode(200)
        .log().all();

  }

}
