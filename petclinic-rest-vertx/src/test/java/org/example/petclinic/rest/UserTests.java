package org.example.petclinic.rest;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import io.vertx.core.Vertx;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.client.WebClient;
import io.vertx.junit5.VertxExtension;
import io.vertx.junit5.VertxTestContext;
import java.util.List;
import java.util.concurrent.TimeUnit;
import org.example.petclinic.PetclinicRestVerticle;
import org.example.petclinic.model.Role;
import org.example.petclinic.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@ExtendWith(VertxExtension.class)
public class UserTests {

  private static final Logger log = LoggerFactory.getLogger(UserTests.class);

  @RegisterExtension
  static PostgresExtension postgresExtension = new PostgresExtension(5432);
  
  static final int PORT = 9966;
  static final String HOST = "localhost";

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
  void testCreateOwnerSuccess(Vertx vertx, VertxTestContext testContext) throws Throwable {
    User user = new User("username", "password", true, List.of(
        new Role("username", "OWNER_ADMIN")));
    JsonObject json = user.toJson();
    log.debug("## json:{}", json);

    client.post(PORT, HOST, "/users")
        .sendJsonObject(json)
        .onComplete(testContext.succeeding(res -> testContext.verify(() -> {
          if (res.statusCode() == 201) {
            testContext.completeNow();
          } else {
            testContext.failNow("Status " + res.statusMessage());
          }
        })));
    assertThat(testContext.awaitCompletion(5, TimeUnit.SECONDS), is(true));
    if (testContext.failed()) {
      throw testContext.causeOfFailure();
    }
  }

}
