package org.example.petclinic.rest;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.client.WebClient;
import io.vertx.junit5.Checkpoint;
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
public class PetclinicTests {

  private static final Logger log = LoggerFactory.getLogger(PetclinicTests.class);

  private static final String PATH = "/petclinic/api";

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
  void addOwner(Vertx vertx, VertxTestContext testContext) throws Throwable {
    JsonObject json = new JsonObject()
        .put("firstName", "Sam")
        .put("lastName", "Schultz")
        .put("address", "4, Evans Streets")
        .put("city", "Wollongong")
        .put("telephone", "444444444");
    client.post(PORT, HOST, PATH + "/owners")
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

  @Test
  void listOwners(Vertx vertx, VertxTestContext testContext) throws Throwable {
    Checkpoint checkpoint = testContext.checkpoint(2);
    client.get(PORT, HOST, PATH + "/owners")
        .send()
        .onComplete(testContext.succeeding(res -> testContext.verify(() -> {
          if (res.statusCode() == 200 && res.bodyAsJsonArray().size() == 10) {
            checkpoint.flag();
          } else {
            testContext.failNow("Status " + res.statusCode());
          }
        })));

    client.get(PORT, HOST, PATH + "/owners")
        .addQueryParam("lastName", "Davis")
        .send()
        .onComplete(testContext.succeeding(res -> testContext.verify(() -> {
          if (res.statusCode() == 200 && res.bodyAsJsonArray().size() == 2) {
            checkpoint.flag();
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
  void getOwner(VertxTestContext testContext) throws Throwable {
    testContext.completeNow();
  }

  @Test
  void updateOwner(VertxTestContext testContext) throws Throwable {
    testContext.completeNow();
  }

  @Test
  void deleteOwner(VertxTestContext testContext) throws Throwable {
    testContext.completeNow();
  }

  @Test
  void addPetToOwner(Vertx vertx, VertxTestContext testContext) throws Throwable {
    JsonObject json = new JsonObject()
        .put("name", "Tom")
        .put("birthDate", "2023-01-01")
        .put("type", new JsonObject()
            .put("id", 1)
            .put("name", "cat"));
    client.post(PORT, HOST, PATH + "/owners/1/pets")
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

  @Test
  void getOwnersPet(VertxTestContext testContext) throws Throwable {
    testContext.completeNow();
  }

  @Test
  void updateOwnersPet(VertxTestContext testContext) throws Throwable {
    testContext.completeNow();
  }

  @Test
  void addVisitToOwner(VertxTestContext testContext) throws Throwable {
    testContext.completeNow();
  }

  @Test
  void listPetTypes(VertxTestContext testContext) throws Throwable {
    testContext.completeNow();
  }

  @Test
  void addPetType(VertxTestContext testContext) throws Throwable {
    testContext.completeNow();
  }

  @Test
  void getPetType(VertxTestContext testContext) throws Throwable {
    testContext.completeNow();
  }

  @Test
  void updatePetType(VertxTestContext testContext) throws Throwable {
    testContext.completeNow();
  }

  @Test
  void deletePetType(VertxTestContext testContext) throws Throwable {
    testContext.completeNow();
  }

  @Test
  void listPets(VertxTestContext testContext) throws Throwable {
    testContext.completeNow();
  }

  @Test
  void addPets(VertxTestContext testContext) throws Throwable {
    testContext.completeNow();
  }

  @Test
  void getPets(VertxTestContext testContext) throws Throwable {
    testContext.completeNow();
  }

  @Test
  void updatePets(VertxTestContext testContext) throws Throwable {
    testContext.completeNow();
  }

  @Test
  void deletePets(VertxTestContext testContext) throws Throwable {
    testContext.completeNow();
  }

  @Test
  void listVisits(VertxTestContext testContext) throws Throwable {
    testContext.completeNow();
  }

  @Test
  void addVisit(VertxTestContext testContext) throws Throwable {
    testContext.completeNow();
  }

  @Test
  void getVisit(VertxTestContext testContext) throws Throwable {
    testContext.completeNow();
  }

  @Test
  void updateVisit(VertxTestContext testContext) throws Throwable {
    testContext.completeNow();
  }

  @Test
  void deleteVisit(VertxTestContext testContext) throws Throwable {
    testContext.completeNow();
  }

  @Test
  void addSpecialty(VertxTestContext testContext) throws Throwable {
    testContext.completeNow();
  }

  @Test
  void getSpecialty(VertxTestContext testContext) throws Throwable {
    testContext.completeNow();
  }

  @Test
  void updateSpecialty(VertxTestContext testContext) throws Throwable {
    testContext.completeNow();
  }

  @Test
  void deleteSpecialty(VertxTestContext testContext) throws Throwable {
    testContext.completeNow();
  }

  @Test
  void listVets(VertxTestContext testContext) throws Throwable {
    testContext.completeNow();
  }

  @Test
  void addVet(VertxTestContext testContext) throws Throwable {
    testContext.completeNow();
  }

  @Test
  void getVet(VertxTestContext testContext) throws Throwable {
    testContext.completeNow();
  }

  @Test
  void updateVet(VertxTestContext testContext) throws Throwable {
    testContext.completeNow();
  }

  @Test
  void deleteVet(VertxTestContext testContext) throws Throwable {
    testContext.completeNow();
  }
}
