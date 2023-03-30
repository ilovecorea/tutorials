package org.exmaple.keycloak;

import static io.restassured.RestAssured.given;

import io.restassured.response.Response;
import org.example.keycloak.SpringBootKeycloakApplication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.web.servlet.MockMvc;

@AutoConfigureMockMvc
@SpringBootTest(classes = SpringBootKeycloakApplication.class, webEnvironment = WebEnvironment.DEFINED_PORT)
public class BaseTest {

  public static final String URL = "http://localhost:9999/auth/realms/site1/protocol/openid-connect/token";
  public static final String CLINET_ID = "app-cli";
  public static final String CLINET_SECRET = null;

  @Autowired
  protected MockMvc mvc;

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
          .post(URL);
    } else {
      response = given()
          .formParam("grant_type", "password")
          .formParam("client_id", clientId)
          .formParam("username", username)
          .formParam("password", password)
          .when()
          .post(URL);
    }

    String accessToken = response.jsonPath().getString("access_token");
    return accessToken;
  }
}
