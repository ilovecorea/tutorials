package com.example.petclinic.service;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.CoreMatchers.startsWith;
import static org.hamcrest.MatcherAssert.assertThat;

import com.example.petclinic.model.Owner;
import com.example.petclinic.model.Pet;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import reactor.test.StepVerifier;

@SpringBootTest
@ActiveProfiles({"local"})
public class ClinicServiceTests extends BaseServiceTests {

  @Autowired
  private ClinicService clinicService;

  private static final Logger log = LoggerFactory.getLogger(ClinicServiceTests.class);

  @Test
  void shouldFindOwnersByLastName() {
    this.clinicService.findOwnerByLastName("Davis")
        .as(StepVerifier::create)
        .expectNextCount(2)
        .verifyComplete();
    this.clinicService.findOwnerByLastName("Daviss")
        .as(StepVerifier::create)
        .expectNextCount(0)
        .verifyComplete();
  }

  @Test
  void shouldFindSingleOwnerWithPet() {
    this.clinicService.findOwnerById(1)
        .as(StepVerifier::create)
        .assertNext(owner -> {
          assertThat(owner.getLastName(), startsWith("Franklin"));
          assertThat(owner.getPets().size(), is(1));
          assertThat(owner.getPets().get(0).getType(), notNullValue());
          assertThat(owner.getPets().get(0).getType().getName(), equalTo("cat"));
        }).verifyComplete();
  }

  @Test
  void shouldInsertOwner() {
    List<Owner> owners = this.clinicService.findOwnerByLastName("Schultz").collectList().block();
    int found = owners.size();

    Owner owner = Owner.builder()
        .firstName("Sam")
        .lastName("Schultz")
        .address("4, Evans Street")
        .city("Wollongong")
        .telephone("4444444444")
        .build();

    this.clinicService.saveOwner(owner)
        .as(StepVerifier::create)
        .assertNext(o -> {
          assertThat(owner.getId().longValue(), not(0));
          assertThat(owner.getPets(), nullValue());
        }).verifyComplete();
    this.clinicService.findOwnerByLastName("Schultz")
        .as(StepVerifier::create)
        .expectNextCount(1)
        .verifyComplete();
  }

  @Test
  void shouldUpdateOwner() {
    Owner owner = this.clinicService.findOwnerById(1).block();
    String oldLastName = owner.getLastName();
    String newLastName = oldLastName + "X";

    owner.setLastName(newLastName);
    this.clinicService.saveOwner(owner).block();
    owner = this.clinicService.findOwnerById(1).block();
    assertThat(owner.getLastName(), equalTo(newLastName));
  }

  @Test
  void shouldFindPetWithCorrectId() {
    Pet pet7 = this.clinicService.findPetById(7).block();
    assertThat(pet7.getName(), startsWith("Samantha"));
    assertThat(pet7.getOwner().getFirstName(), equalTo("Jean"));
  }
}
