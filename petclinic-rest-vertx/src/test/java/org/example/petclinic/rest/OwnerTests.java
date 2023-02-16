package org.example.petclinic.rest;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.CoreMatchers.*;

import io.vertx.core.Vertx;
import io.vertx.ext.web.client.WebClient;
import io.vertx.junit5.VertxExtension;
import io.vertx.junit5.VertxTestContext;
import java.util.concurrent.TimeUnit;
import org.example.petclinic.PetclinicRestVerticle;
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
  void shouldFindOwnersByLastName(Vertx vertx, VertxTestContext testContext) throws Throwable {
    client.get(9966, "localhost", "/owners")
        .send()
        .onComplete(testContext.succeeding(res -> testContext.verify(() -> {
          if (res.statusCode() == 200) {
            testContext.completeNow();
          } else {
            testContext.failNow("Status " + res.statusCode());
          }
        })));
//    assertThat(testContext.awaitCompletion(5, TimeUnit.SECONDS), is(true));
//    if (testContext.failed()) {
//      throw testContext.causeOfFailure();
//    }
  }

}
