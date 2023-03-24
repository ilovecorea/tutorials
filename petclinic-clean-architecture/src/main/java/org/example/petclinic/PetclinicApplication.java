package org.example.petclinic;

import io.vertx.core.Vertx;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Configuration;

@SpringBootApplication
@Configuration
public class PetclinicApplication {

  @Autowired
  private PetclinicVerticle petclinicVerticle;

  public static void main(String[] args) {
    SpringApplication.run(PetclinicApplication.class, args);
  }

  @PostConstruct
  public void deployVerticle() {
    final Vertx vertx = Vertx.vertx();
    vertx.deployVerticle(petclinicVerticle);
  }
}
