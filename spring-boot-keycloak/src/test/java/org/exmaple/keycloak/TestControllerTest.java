package org.exmaple.keycloak;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

@Slf4j
public class TestControllerTest extends BaseTest {

  @Test
  public void testAdmin() throws Exception {
    String accessToken = obtainAccessToken(URL, CLINET_ID, CLINET_SECRET, "admin", "password");
    this.mvc.perform(MockMvcRequestBuilders.get("/test/admin")
            .contentType(MediaType.APPLICATION_JSON)
            .header("Authorization", "Bearer " + accessToken))
        .andExpect(status().isOk());
//    given()
//        .auth().preemptive().oauth2(accessToken)
//        .when()
//        .get("/api/test/admin")
//        .then()
//        .statusCode(200)
//        .assertThat()
//        .body("userName", is("admin"))
//        .body("userEmail", is("admin@example.com"));
  }

  @Test
  public void testUser() throws Exception {
    String accessToken = obtainAccessToken(URL, CLINET_ID, CLINET_SECRET, "user", "password");
    this.mvc.perform(MockMvcRequestBuilders.get("/test/user")
            .contentType(MediaType.APPLICATION_JSON)
            .header("Authorization", "Bearer " + accessToken))
        .andExpect(status().isOk());
//    log.info("accessToken:{}", accessToken);
//    given()
//        .auth().preemptive().oauth2(accessToken)
//        .when()
//        .get("/api/test/user")
//        .then()
//        .statusCode(200)
//        .assertThat()
//        .body("userName", is("user"))
//        .body("userEmail", is("user@example.com"));
  }

  @Test
  public void testForbidden() throws Exception {
    String accessToken = obtainAccessToken(URL, CLINET_ID, CLINET_SECRET, "user", "password");
    this.mvc.perform(MockMvcRequestBuilders.get("/test/admin")
            .contentType(MediaType.APPLICATION_JSON)
            .header("Authorization", "Bearer " + accessToken))
        .andExpect(status().isForbidden());
//    log.info("accessToken:{}", accessToken);
//    given()
//        .auth().preemptive().oauth2(accessToken)
//        .when()
//        .get("/api/test/admin")
//        .then()
//        .statusCode(403);
  }

}
