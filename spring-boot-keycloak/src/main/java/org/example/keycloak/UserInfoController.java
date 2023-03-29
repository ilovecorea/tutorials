package org.example.keycloak;

import com.example.keycloak.UserInfoDto;
import com.example.keycloak.UsersApi;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api")
public class UserInfoController implements UsersApi {

  @Override
  @PreAuthorize("hasAuthority('SCOPE_email') and hasAuthority('SCOPE_profile') and hasAuthority('ROLE_user')")
  public ResponseEntity<UserInfoDto> getUserInfo() {
    SecurityContext sc = SecurityContextHolder.getContext();
    Authentication authentication = sc.getAuthentication();
    Jwt jwt = (Jwt) authentication.getPrincipal();
    String sub = (String) jwt.getClaims().getOrDefault("sub", "");
    String username = (String) jwt.getClaims().getOrDefault("preferred_username", "");
    String email = (String) jwt.getClaims().getOrDefault("email", "");
    UserInfoDto userInfoDto = new UserInfoDto();
    userInfoDto.setSub(sub);
    userInfoDto.setName(username);
    userInfoDto.setEmail(email);

    return ResponseEntity.ok(userInfoDto);
  }
}
