package org.example.petclinic.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "database")
public class DatabaseConfig {

  private String host;

  private Integer port;

  private String database;

  private String username;

  private String password;

  private Integer maxPoolSize;

}
