package com.example.petclinic.repository;

import static io.restassured.RestAssured.when;

import io.restassured.RestAssured;
import org.example.petclinic.PetclinicDataRestApplication;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;

@SpringBootTest(classes = PetclinicDataRestApplication.class, webEnvironment = WebEnvironment.DEFINED_PORT)
public class PetTypeLiveTest {

  @BeforeAll
  public static void init() {
    RestAssured.baseURI = "http://localhost:8080";
    RestAssured.port = 8080;
  }

  @Test
  public void testFindAllOk() {
    when()
        .get("/types")
        .prettyPrint();
  }
}
