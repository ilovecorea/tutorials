package org.exmaple.keycloak;

import static io.restassured.RestAssured.given;

import io.restassured.response.Response;

public class BaseTest {

  public static final String URL = "http://localhost:9999/auth/realms/site1/protocol/openid-connect/token";
  public static final String CLINET_ID = "site1-client";
  public static final String CLINET_SECRET = null;

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
