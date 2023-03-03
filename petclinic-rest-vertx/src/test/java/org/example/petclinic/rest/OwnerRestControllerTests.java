package org.example.petclinic.rest;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;

import io.vertx.core.Future;
import io.vertx.core.Vertx;
import io.vertx.ext.web.client.WebClient;
import io.vertx.junit5.VertxExtension;
import io.vertx.junit5.VertxTestContext;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import org.example.petclinic.PetclinicRestVerticle;
import org.example.petclinic.model.Owner;
import org.example.petclinic.model.Pet;
import org.example.petclinic.model.PetType;
import org.example.petclinic.model.Visit;
import org.example.petclinic.persistence.impl.OwnerPersistenceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(value = {VertxExtension.class, MockitoExtension.class})
public class OwnerRestControllerTests {

  static final int PORT = 9966;

  static final String HOST = "localhost";

  static final String PATH = "/petclinic/api";

  @Mock
  OwnerPersistenceImpl ownersPersistence;

  private List<Owner> owners;

  private List<Pet> pets;

  private List<Visit> visits;

  WebClient client;

  @BeforeEach
  void initOwners(Vertx vertx, VertxTestContext testContext) {

    System.setProperty("activeProfile", "mock");

    owners = new ArrayList<>();
    Owner ownerWithPet = new Owner();
    owners.add(ownerWithPet
        .setId(1)
        .setFirstName("George")
        .setLastName("Franklin")
        .setAddress("110 W. Liberty St.")
        .setCity("Madison")
        .setTelephone("6085551023")
        .setPets(List.of(getTestPetWithIdAndName(ownerWithPet, 1, "Rosy"))));
    Owner owner = new Owner();
    owners.add(owner.setId(2)
        .setFirstName("Betty")
        .setLastName("Davis")
        .setAddress("638 Cardinal Ave.")
        .setCity("Sun Prairie")
        .setTelephone("6085551749"));
    owner = new Owner();
    owners.add(owner.setId(3)
        .setFirstName("Eduardo")
        .setLastName("Rodriquez")
        .setAddress("2693 Commerce St.")
        .setCity("McFarland")
        .setTelephone("6085558763"));
    owner = new Owner();
    owners.add(owner.setId(4)
        .setFirstName("Harold")
        .setLastName("Davis")
        .setAddress("563 Friendly St.")
        .setCity("Windsor")
        .setTelephone("6085553198"));

    PetType petType = new PetType();
    petType.setId(2)
        .setName("dog");

    pets = new ArrayList<>();
    Pet pet = new Pet();
    pets.add(pet.setId(3)
        .setName("Rosy")
        .setBirthDate(LocalDate.now().toString())
        .setType(petType));

    pet = new Pet();
    pets.add(pet.setId(4)
        .setName("Jewel")
        .setBirthDate(LocalDate.now().toString())
        .setType(petType));

    visits = new ArrayList<>();
    Visit visit = new Visit();
    visit.setId(2);
    visit.setPetId(pet.getId());
    visit.setDate(LocalDate.now().toString());
    visit.setDescription("rabies shot");
    visits.add(visit);

    visit = new Visit();
    visit.setId(3);
    visit.setPetId(pet.getId());
    visit.setDate(LocalDate.now().toString());
    visit.setDescription("neutered");
    visits.add(visit);

    vertx.deployVerticle(new PetclinicRestVerticle(),
        testContext.succeeding(id -> testContext.verify(() -> testContext.completeNow())));
    client = WebClient.create(vertx);
  }

  private Pet getTestPetWithIdAndName(final Owner owner, final int id, final String name) {
    PetType petType = new PetType();
    Pet pet = new Pet();
    pet.setId(id)
        .setName(name)
        .setBirthDate(LocalDate.now().toString())
        .setType(petType
            .setId(2)
            .setName("dog"))
        .setVisits(List.of(getTestVisitForPet(pet, 1)));
    return pet;
  }

  private Visit getTestVisitForPet(final Pet pet, final int id) {
    Visit visit = new Visit();
    return visit
        .setId(id)
        .setDate(LocalDate.now().toString()).setDescription("test" + id);
  }

  @Test
  void testGetOwnerSuccess(VertxTestContext testContext) throws Throwable {
    when(ownersPersistence.findById(anyInt()))
        .thenReturn(Future.succeededFuture(Optional.empty()));
    client.get(PORT, HOST, PATH + "/owners/1")
        .send()
        .onComplete(testContext.succeeding(res -> testContext.verify(() -> {
          if (res.statusCode() == 200) {
            testContext.completeNow();
          } else {
            testContext.failNow("Error:" + res.statusMessage());
          }
        })));
    assertThat(testContext.awaitCompletion(5, TimeUnit.SECONDS), is(true));
    if (testContext.failed()) {
      throw testContext.causeOfFailure();
    }
  }
}
