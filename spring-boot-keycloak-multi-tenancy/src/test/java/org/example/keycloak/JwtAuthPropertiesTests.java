package org.example.keycloak;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@Slf4j
@SpringBootTest
public class JwtAuthPropertiesTests {

  @Autowired
  private JwtAuthProperties jwtAuthProperties;

  @Test
  void contextLoad() {
    Assertions.assertNotNull(jwtAuthProperties);
    log.info("jwtAuthProperties:{}", jwtAuthProperties);
  }

}
