package org.exmaple.keycloak;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

@Slf4j
public class UserInfoControllerTest extends BaseTest {

  @Test
  public void testUserInfo() throws Exception {
    String accessToken = obtainAccessToken(URL, CLINET_ID, CLINET_SECRET, "user", "password");
    this.mvc.perform(get("/api/users/me")
            .contentType(MediaType.APPLICATION_JSON)
            .header("Authorization", "Bearer " + accessToken))
        .andExpect(status().isOk());
//    given()
//        .auth().preemptive().oauth2(accessToken)
//        .when()
//        .get("/users/me")
//        .then()
//        .statusCode(200)
//        .log().all();
  }

}
