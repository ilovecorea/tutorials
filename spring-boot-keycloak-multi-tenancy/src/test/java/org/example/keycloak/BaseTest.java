package org.example.keycloak;

import static io.restassured.RestAssured.given;
import static io.restassured.RestAssured.when;

import io.restassured.response.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

@AutoConfigureMockMvc
@SpringBootTest(classes = SpringBootKeycloakMultiTenancyApplication.class)
public class BaseTest {

  @Autowired
  protected MockMvc mvc;

  protected String obtainTokenEndpoint(String url) {
    Response response = when().get(url);
    String tokenEndpoint = response.jsonPath().getString("token-service");
    return tokenEndpoint + "/token";
  }

  protected String obtainAccessToken(String url, String clientId, String clientSecret,
      String username, String password) {
    Response response = null;
    if (clientSecret != null) {
      response = given()
          .auth().preemptive().basic(clientId, clientSecret)
          .formParam("grant_type", "password")
          .formParam("username", username)
          .formParam("password", password)
          .when()
          .post(url);
    } else {
      response = given()
          .formParam("grant_type", "password")
          .formParam("client_id", clientId)
          .formParam("username", username)
          .formParam("password", password)
          .when()
          .post(url);
    }

    String accessToken = response.jsonPath().getString("access_token");
    return accessToken;
  }
}
