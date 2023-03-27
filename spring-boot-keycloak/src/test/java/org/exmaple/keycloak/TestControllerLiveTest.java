package org.exmaple.keycloak;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.is;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import lombok.extern.slf4j.Slf4j;
import org.example.keycloak.SpringBootKeycloakApplication;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;

@Slf4j
@SpringBootTest(classes = SpringBootKeycloakApplication.class, webEnvironment = WebEnvironment.DEFINED_PORT)
public class TestControllerLiveTest {

  static final String URL = "http://localhost:9000/auth/realms/lemonhc/protocol/openid-connect/token";
  static final String CLINET_ID = "lemonhc-app";
  static final String CLINET_SECRET = "HeRBYSxaVbuMuC4GYs4Kj0MKk6VIb5os";

  @BeforeAll
  public static void init() {
    RestAssured.port = 8080;
  }

  @Test
  public void testAdmin() {
    String accessToken = obtainAccessToken(URL, CLINET_ID, CLINET_SECRET, "admin1", "password");
    log.info("accessToken:{}", accessToken);
    given()
        .auth().preemptive().oauth2(accessToken)
        .when()
        .get("/api/test/admin")
        .then()
        .statusCode(200)
        .assertThat()
        .body("userName", is("admin1"))
        .body("userEmail", is("admin1@lemonhc.com"));
  }

  @Test
  public void testUser() {
    String accessToken = obtainAccessToken(URL, CLINET_ID, CLINET_SECRET, "user1", "password");
    log.info("accessToken:{}", accessToken);
    given()
        .auth().preemptive().oauth2(accessToken)
        .when()
        .get("/api/test/user")
        .then()
        .statusCode(200)
        .assertThat()
        .body("userName", is("user1"))
        .body("userEmail", is("user1@lemonhc.com"));
  }

  @Test
  public void testForbidden() {
    String accessToken = obtainAccessToken(URL, CLINET_ID, CLINET_SECRET, "user1", "password");
    log.info("accessToken:{}", accessToken);
    given()
        .auth().preemptive().oauth2(accessToken)
        .when()
        .get("/api/test/admin")
        .then()
        .statusCode(403);
  }

  private String obtainAccessToken(String url, String clientId, String clientSecret,
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
