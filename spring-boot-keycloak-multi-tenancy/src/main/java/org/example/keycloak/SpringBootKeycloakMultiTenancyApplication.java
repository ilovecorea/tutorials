package org.example.keycloak;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@ConfigurationPropertiesScan
@EnableConfigurationProperties
@SpringBootApplication
public class SpringBootKeycloakMultiTenancyApplication {

  public static void main(String[] args) {
    SpringApplication.run(SpringBootKeycloakMultiTenancyApplication.class, args);
  }
}