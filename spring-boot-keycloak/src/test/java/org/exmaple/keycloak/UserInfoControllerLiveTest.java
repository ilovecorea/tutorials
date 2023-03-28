package org.exmaple.keycloak;

import static io.restassured.RestAssured.given;

import lombok.extern.slf4j.Slf4j;
import org.example.keycloak.SpringBootKeycloakApplication;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;

@Slf4j
@SpringBootTest(classes = SpringBootKeycloakApplication.class, webEnvironment = WebEnvironment.DEFINED_PORT)
public class UserInfoControllerLiveTest extends BaseTest {

  @Test
  public void testUserInfo() {
    String accessToken = obtainAccessToken(URL, CLINET_ID, CLINET_SECRET, "user", "password");
    log.info("accessToken:{}", accessToken);
    given()
        .auth().preemptive().oauth2(accessToken)
        .when()
        .get("/api/userInfo")
        .then()
        .statusCode(200)
        .log().all();
  }

}
