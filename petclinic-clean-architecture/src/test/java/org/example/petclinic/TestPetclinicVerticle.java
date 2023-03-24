package org.example.petclinic;

import static org.assertj.core.api.Assertions.assertThat;

import io.vertx.core.Vertx;
import io.vertx.core.http.HttpClientOptions;
import io.vertx.core.http.HttpClientResponse;
import io.vertx.core.http.HttpMethod;
import io.vertx.core.spi.VerticleFactory;
import io.vertx.junit5.VertxExtension;
import io.vertx.junit5.VertxTestContext;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;

@Slf4j
@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ExtendWith(VertxExtension.class)
public class TestPetclinicVerticle {

  @Autowired
  ApplicationContext context;

  Vertx vertx;

  @BeforeAll
  public void setupAll(VertxTestContext testContext) {
    vertx = context.getBean(Vertx.class);
    var factory = context.getBean(VerticleFactory.class);
    vertx.deployVerticle(factory.prefix() + ":" + PetclinicVerticle.class.getName())
        .onSuccess(id -> {
          log.info("deployed:" + id);
          testContext.completeNow();
        })
        .onFailure(testContext::failNow);
  }

  @Test
  public void testVertx(VertxTestContext testContext) {
    assertThat(vertx).isNotNull();
    testContext.completeNow();
  }

  @Test
  public void testListPetType(VertxTestContext testContext) {
    var options = new HttpClientOptions()
        .setDefaultPort(8080);
    var client = vertx.createHttpClient(options);
    client.request(HttpMethod.GET, "/pettypes")
        .flatMap(req -> req.send().flatMap(HttpClientResponse::body))
        .onSuccess(buffer -> testContext.verify(() -> {
          log.info("buffer:{}", buffer.toJson());
          testContext.completeNow();
        }))
        .onFailure(testContext::failNow);
  }
}
