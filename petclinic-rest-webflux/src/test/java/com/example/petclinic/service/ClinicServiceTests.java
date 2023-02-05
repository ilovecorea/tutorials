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
import com.example.petclinic.model.PetType;
import com.example.petclinic.model.Vet;
import com.example.petclinic.model.Visit;
import com.example.petclinic.util.EntityUtils;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
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
  void shouldFindOwnerWithPetsByOwnerId() {
    Owner owner = this.clinicService.findOwnerById(3).block();
    log.debug("owner:{}", owner);
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

//  @Test
//  void shouldInsertOwner() {
//    List<Owner> owners = this.clinicService.findOwnerByLastName("Schultz").collectList().block();
//    int found = owners.size();
//
//    Owner owner = Owner.builder()
//        .firstName("Sam")
//        .lastName("Schultz")
//        .address("4, Evans Street")
//        .city("Wollongong")
//        .telephone("4444444444")
//        .build();
//
//    this.clinicService.saveOwner(owner)
//        .as(StepVerifier::create)
//        .assertNext(o -> {
//          assertThat(owner.getId().longValue(), not(0));
//          assertThat(owner.getPets(), nullValue());
//        }).verifyComplete();
//    this.clinicService.findOwnerByLastName("Schultz")
//        .as(StepVerifier::create)
//        .expectNextCount(1)
//        .verifyComplete();
//  }
//
//  @Test
//  void shouldUpdateOwner() {
//    Owner owner = this.clinicService.findOwnerById(1).block();
//    String oldLastName = owner.getLastName();
//    String newLastName = oldLastName + "X";
//
//    owner.setLastName(newLastName);
//    this.clinicService.saveOwner(owner).block();
//    owner = this.clinicService.findOwnerById(1).block();
//    assertThat(owner.getLastName(), equalTo(newLastName));
//  }
//
//  @Test
//  void shouldFindPetWithCorrectId() {
//    Pet pet7 = this.clinicService.findPetById(7).block();
//    assertThat(pet7.getName(), startsWith("Samantha"));
//    assertThat(pet7.getOwner().getFirstName(), equalTo("Jean"));
//  }
//
//  @Test
//  void shouldInsertPetIntoDatabaseAndGenerateId() {
//    Owner owner6 = this.clinicService.findOwnerById(6).block();
//    int found = owner6.getPets().size();
//
//    Pet pet = new Pet();
//    pet.setName("bowser");
//    Collection<PetType> types = this.clinicService.findPetTypes().collectList().block();
//    pet.setType(EntityUtils.getById(types, PetType.class, 2));
//    pet.setBirthDate(LocalDate.now());
//    List<Pet> pets = new ArrayList<>();
//    pets.addAll(owner6.getPets());
//    pets.add(pet);
//    owner6.setPets(pets);
//    this.clinicService.saveOwner(owner6).block();
//    assertThat(owner6.getPets().size(), is(found + 1));
//
//    owner6 = this.clinicService.findOwnerById(6).block();
//    assertThat(owner6.getPets().size(), is(found + 1));
//    assertThat(pet.getId(), notNullValue());
//  }
//
//  @Test
//  void shouldUpdatePetName() throws Exception {
//    Pet pet7 = this.clinicService.findPetById(7).block();
//    String oldName = pet7.getName();
//
//    String newName = oldName + "X";
//    pet7.setName(newName);
//    this.clinicService.savePet(pet7).block();
//
//    pet7 = this.clinicService.findPetById(7).block();
//    assertThat(pet7.getName(), equalTo(newName));
//  }
//
//  @Test
//  void shouldFindVets() {
//    Collection<Vet> vets = this.clinicService.findVets().collectList().block();
//
//    Vet vet = EntityUtils.getById(vets, Vet.class, 3);
//    assertThat(vet.getLastName(), equalTo("Douglas"));
//    assertThat(vet.getSpecialties().size(), is(2));
//    assertThat(vet.getSpecialties().get(0).getName(), equalTo("dentistry"));
//    assertThat(vet.getSpecialties().get(1).getName(), equalTo("surgery"));
//  }
//
//  @Test
//  void shouldAddNewVisitForPet() {
//    Pet pet7 = this.clinicService.findPetById(7).block();
//    int found = pet7.getVisits().size();
//    Visit visit = new Visit();
////    pet7.addVisit(visit);
//    visit.setDescription("test");
//    List<Visit> visits = new ArrayList<>();
//    visits.addAll(pet7.getVisits());
//    visits.add(visit);
//    this.clinicService.saveVisit(visit);
//    this.clinicService.savePet(pet7);
//
////    pet7 = this.clinicService.findPetById(7).block();
////    assertThat(pet7.getVisits().size(), is(found + 1));
////    assertThat(visit.getId(), notNullValue());
//  }
}
