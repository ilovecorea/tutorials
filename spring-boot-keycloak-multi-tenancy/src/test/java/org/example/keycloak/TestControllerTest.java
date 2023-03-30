package org.example.keycloak;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import lombok.extern.slf4j.Slf4j;
import org.example.keycloak.JwtAuthProperties.Issuer;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

@Slf4j
public class TestControllerTest extends BaseTest {

  @Autowired
  private JwtAuthProperties properties;

  @Test
  public void testIssuer1Admin() throws Exception {
    Issuer issuer = properties.getAuth().get("issuer1");
    String url = obtainTokenEndpoint(issuer.getIssuerUri());
    String accessToken = obtainAccessToken(url, issuer.getResourceId(), null, "admin", "password");
    this.mvc.perform(MockMvcRequestBuilders.get("/test/admin")
            .contentType(MediaType.APPLICATION_JSON)
            .header("Authorization", "Bearer " + accessToken))
        .andExpect(status().isOk());
  }

  @Test
  public void testIssuer1User() throws Exception {
    Issuer issuer = properties.getAuth().get("issuer1");
    String url = obtainTokenEndpoint(issuer.getIssuerUri());
    String accessToken = obtainAccessToken(url, issuer.getResourceId(), null, "user", "password");
    this.mvc.perform(MockMvcRequestBuilders.get("/test/user")
            .contentType(MediaType.APPLICATION_JSON)
            .header("Authorization", "Bearer " + accessToken))
        .andExpect(status().isOk());
  }

  @Test
  public void testIssuer1Forbidden() throws Exception {
    Issuer issuer = properties.getAuth().get("issuer1");
    String url = obtainTokenEndpoint(issuer.getIssuerUri());
    String accessToken = obtainAccessToken(url, issuer.getResourceId(), null, "user", "password");
    this.mvc.perform(MockMvcRequestBuilders.get("/test/admin")
            .contentType(MediaType.APPLICATION_JSON)
            .header("Authorization", "Bearer " + accessToken))
        .andExpect(status().isForbidden());
  }

  @Test
  public void testIssuer2Admin() throws Exception {
    Issuer issuer = properties.getAuth().get("issuer2");
    String url = obtainTokenEndpoint(issuer.getIssuerUri());
    String accessToken = obtainAccessToken(url, issuer.getResourceId(), null, "admin", "password");
    this.mvc.perform(MockMvcRequestBuilders.get("/test/admin")
            .contentType(MediaType.APPLICATION_JSON)
            .header("Authorization", "Bearer " + accessToken))
        .andExpect(status().isOk());
  }

  @Test
  public void testIssuer2User() throws Exception {
    Issuer issuer = properties.getAuth().get("issuer2");
    String url = obtainTokenEndpoint(issuer.getIssuerUri());
    String accessToken = obtainAccessToken(url, issuer.getResourceId(), null, "user", "password");
    this.mvc.perform(MockMvcRequestBuilders.get("/test/user")
            .contentType(MediaType.APPLICATION_JSON)
            .header("Authorization", "Bearer " + accessToken))
        .andExpect(status().isOk());
  }

  @Test
  public void testIssuer2Forbidden() throws Exception {
    Issuer issuer = properties.getAuth().get("issuer2");
    String url = obtainTokenEndpoint(issuer.getIssuerUri());
    String accessToken = obtainAccessToken(url, issuer.getResourceId(), null, "user", "password");
    this.mvc.perform(MockMvcRequestBuilders.get("/test/admin")
            .contentType(MediaType.APPLICATION_JSON)
            .header("Authorization", "Bearer " + accessToken))
        .andExpect(status().isForbidden());
  }
}
