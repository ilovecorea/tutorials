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
public class PetTypeRepositoryLiveTest {

  @BeforeAll
  public static void init() {
    RestAssured.baseURI = "http://localhost:8080";
    RestAssured.port = 8080;
  }

  @Test
  public void testFindAllOk() {
    when()
        .get("/types")
        .then()
        .assertThat()
        .statusCode(200)
        .body("page.size", is(10))
        .log().all();

  }

  @Test
  public void testFindAllWithProjectionOk() {
    when()
        .get("/types?projection=petType")
        .then()
        .assertThat()
        .statusCode(200)
        .body("page.size", is(10))
        .log().all();

  }

  @Test
  public void testFindAllWithPageOk() {
    when()
        .get("/types?projection=petType&size=5")
        .then()
        .assertThat()
        .statusCode(200)
        .body("page.size", is(5))
        .log().all();

  }

  @Test
  public void testFindOk() {
    when()
        .get("/types/1?projection=petType")
        .then()
        .assertThat()
        .statusCode(200)
        .log().all();
  }

  @Test
  public void testCreateOk() {
    PetType petType = new PetType();
    petType.setName("elephant");
    given()
        .contentType(ContentType.JSON)
        .body(petType)
        .when()
        .post("/types")
        .then()
        .statusCode(201);

    when()
        .get("/types/search/findByName?projection=petType&name=elephant")
        .then()
        .statusCode(200)
        .log().all();
  }

  @Test
  public void testSaveBadRequest() {
    PetType petType = new PetType();
    given()
        .contentType(ContentType.JSON)
        .body(petType)
        .when()
        .put("/types/1")
        .then()
        .statusCode(400)
        .log().all();
  }
}
