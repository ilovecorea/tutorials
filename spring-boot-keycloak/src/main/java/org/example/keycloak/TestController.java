package org.example.keycloak;

import java.security.Principal;
import java.util.Map;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/test")
public class TestController {

  @GetMapping(value = "/admin", produces = "application/json")
  public Map<String, Object> getAdmin(Principal principal) {
    JwtAuthenticationToken token = (JwtAuthenticationToken) principal;
    String userName = (String) token.getTokenAttributes().get("preferred_username");
    String userEmail = (String) token.getTokenAttributes().get("email");
    return Map.of("userName", userName, "userEmail", userEmail);
  }

  @GetMapping("/user")
  public Map<String, Object> getUser(Principal principal) {
    JwtAuthenticationToken token = (JwtAuthenticationToken) principal;
    String userName = (String) token.getTokenAttributes().get("preferred_username");
    String userEmail = (String) token.getTokenAttributes().get("email");
    return Map.of("userName", userName, "userEmail", userEmail);
  }

}