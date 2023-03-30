package org.example.keycloak;

import java.util.Map;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "jwt")
public class JwtAuthProperties {

  private Map<String, Issuer> auth;

  @Data
  public static class Issuer {

    private String issuerUri;

    private String resourceId;

    private String principalAttribute;

  }
}
