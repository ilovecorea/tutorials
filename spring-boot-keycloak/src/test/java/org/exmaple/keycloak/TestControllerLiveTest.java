package org.exmaple.keycloak;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.is;

import lombok.extern.slf4j.Slf4j;
import org.example.keycloak.SpringBootKeycloakApplication;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;

@Slf4j
@SpringBootTest(classes = SpringBootKeycloakApplication.class, webEnvironment = WebEnvironment.DEFINED_PORT)
public class TestControllerLiveTest extends BaseTest {

  @Test
  public void testAdmin() {
    String accessToken = obtainAccessToken(URL, CLINET_ID, CLINET_SECRET, "admin", "password");
    given()
        .auth().preemptive().oauth2(accessToken)
        .when()
        .get("/api/test/admin")
        .then()
        .statusCode(200)
        .assertThat()
        .body("userName", is("admin"))
        .body("userEmail", is("admin@example.com"));
  }

  @Test
  public void testUser() {
    String accessToken = obtainAccessToken(URL, CLINET_ID, CLINET_SECRET, "user", "password");
    log.info("accessToken:{}", accessToken);
    given()
        .auth().preemptive().oauth2(accessToken)
        .when()
        .get("/api/test/user")
        .then()
        .statusCode(200)
        .assertThat()
        .body("userName", is("user"))
        .body("userEmail", is("user@example.com"));
  }

  @Test
  public void testForbidden() {
    String accessToken = obtainAccessToken(URL, CLINET_ID, CLINET_SECRET, "user", "password");
    log.info("accessToken:{}", accessToken);
    given()
        .auth().preemptive().oauth2(accessToken)
        .when()
        .get("/api/test/admin")
        .then()
        .statusCode(403);
  }

}
