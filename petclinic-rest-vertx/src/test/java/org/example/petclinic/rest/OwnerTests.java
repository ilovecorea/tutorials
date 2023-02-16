package org.example.petclinic.rest;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.CoreMatchers.*;

import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.client.WebClient;
import io.vertx.junit5.VertxExtension;
import io.vertx.junit5.VertxTestContext;
import java.util.concurrent.TimeUnit;
import org.example.petclinic.PetclinicRestVerticle;
import org.example.petclinic.model.Pet;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@ExtendWith(VertxExtension.class)
public class OwnerTests {

  private static final Logger log = LoggerFactory.getLogger(OwnerTests.class);

  @RegisterExtension
  static PostgresExtension postgresExtension = new PostgresExtension(5432);

  WebClient client;

  @BeforeEach
  void setUp(Vertx vertx, VertxTestContext testContext) {
    vertx.deployVerticle(new PetclinicRestVerticle(),
        testContext.succeeding(id -> testContext.verify(() -> {
          log.debug("## deployId:{}", id);
          testContext.completeNow();
        })));
    client = WebClient.create(vertx);
  }

  @Test
  void shouldFindOwners(Vertx vertx, VertxTestContext testContext) throws Throwable {
    client.get(9966, "localhost", "/owners")
        .send()
        .onComplete(testContext.succeeding(res -> testContext.verify(() -> {
          if (res.statusCode() == 200 && res.bodyAsJsonArray().size() == 10) {
            testContext.completeNow();
          } else {
            testContext.failNow("Status " + res.statusCode());
          }
        })));
    assertThat(testContext.awaitCompletion(5, TimeUnit.SECONDS), is(true));
    if (testContext.failed()) {
      throw testContext.causeOfFailure();
    }
  }

  @Test
  void shouldFindOwnersByLastName(Vertx vertx, VertxTestContext testContext) throws Throwable {
    client.get(9966, "localhost", "/owners")
        .addQueryParam("lastName", "Davis")
        .send()
        .onComplete(testContext.succeeding(res -> testContext.verify(() -> {
          if (res.statusCode() == 200 && res.bodyAsJsonArray().size() == 2) {
            testContext.completeNow();
          } else {
            testContext.failNow("Status " + res.statusCode());
          }
        })));
    assertThat(testContext.awaitCompletion(5, TimeUnit.SECONDS), is(true));
    if (testContext.failed()) {
      throw testContext.causeOfFailure();
    }
  }

  @Test
  void testCreatePetSuccess(Vertx vertx, VertxTestContext testContext) throws Throwable {
    JsonObject json = new JsonObject()
        .put("name", "Tom")
        .put("birthDate", "2023-01-01")
        .put("type", new JsonObject()
            .put("id", 1)
            .put("name", "cat"));
    client.post(9966, "localhost", "/owners/1/pets")
        .sendJsonObject(json)
        .onComplete(testContext.succeeding(res -> testContext.verify(() -> {
          if (res.statusCode() == 201) {
            testContext.completeNow();
          } else {
            testContext.failNow("Status " + res.statusCode());
          }
        })));
    assertThat(testContext.awaitCompletion(5, TimeUnit.SECONDS), is(true));
    if (testContext.failed()) {
      throw testContext.causeOfFailure();
    }
  }
}
