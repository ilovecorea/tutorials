package com.example.petclinic.service;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.CoreMatchers.startsWith;
import static org.hamcrest.MatcherAssert.assertThat;

import com.example.petclinic.model.Owner;
import com.example.petclinic.model.Pet;
import com.example.petclinic.model.PetType;
import com.example.petclinic.model.Specialty;
import com.example.petclinic.model.Vet;
import com.example.petclinic.model.Visit;
import com.example.petclinic.util.EntityUtils;
import java.time.LocalDate;
import java.util.Collection;
import java.util.List;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.reactive.TransactionalOperator;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@SpringBootTest
@ActiveProfiles({"local"})
public class ClinicServiceTests extends BaseServiceTests {

  @Autowired
  private ClinicService clinicService;

  @Autowired
  private TransactionalOperator tx;

  private static final Logger log = LoggerFactory.getLogger(ClinicServiceTests.class);

  /**
   * StepVerifier를 통한 검증
   */
  @Test
  @Order(1)
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

  /**
   * StepVerifier를 통한 검증
   */
  @Test
  @Order(2)
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
  @Order(3)
  void shouldInsertOwner() {

    final List<Owner> owners = this.clinicService.findOwnerByLastName("Schultz").collectList()
        .block();
    final int found = owners.size();

    final Owner owner = Owner.builder()
        .firstName("Sam")
        .lastName("Schultz")
        .address("4, Evans Street")
        .city("Wollongong")
        .telephone("4444444444")
        .build();

//    Owner owner1 = clinicService.saveOwner(owner).block();
//    assertThat(owner1.getId().longValue(), not(0));
//    assertThat(owner1.getPets(), nullValue());
//    List<Owner> owners1 = clinicService.findOwnerByLastName("Schultz").collectList().block();
//    assertThat(owners1.size(), is(found + 1));

    tx.execute(status -> {
      Mono<Owner> save = clinicService.saveOwner(owner);
      Mono<List<Owner>> find = clinicService.findOwnerByLastName("Schultz").collectList();

      return save.then(find.handle((owners1, synchronousSink) -> {
        if (owners1.size() == found + 1) {
          status.setRollbackOnly();
        }
      }));
    }).as(StepVerifier::create).verifyComplete();
  }

  @Test
  @Order(4)
  void shouldUpdateOwner() {

    final Owner owner = this.clinicService.findOwnerById(1).block();
    final String oldLastName = owner.getLastName();
    final String newLastName = oldLastName + "X";

    owner.setLastName(newLastName);
    this.clinicService.saveOwner(owner).block();
//    owner = this.clinicService.findOwnerById(1).block();
//    assertThat(owner.getLastName(), equalTo(newLastName));

    tx.execute(status -> {
      Mono<Owner> save = clinicService.saveOwner(owner);
      Mono<Owner> find = clinicService.findOwnerById(1);

      return save.then(find.handle((o, sink) -> {
        if (o.getLastName().equals(newLastName)) {
          status.setRollbackOnly();
        }
      }));
    }).as(StepVerifier::create).verifyComplete();
  }

  @Test
  @Order(5)
  void shouldFindPetWithCorrectId() {
    clinicService.findPetById(7)
        .as(StepVerifier::create)
        .assertNext(pet -> {
          assertThat(pet.getName(), equalTo("Samantha"));
          assertThat(pet.getOwner().getFirstName(), equalTo("Jean"));
        });

//    Pet pet7 = this.clinicService.findPetById(7).block();
//    assertThat(pet7.getName(), startsWith("Samantha"));
//    assertThat(pet7.getOwner().getFirstName(), equalTo("Jean"));
  }

  @Test
  @Order(6)
  void shouldInsertPetIntoDatabaseAndGenerateId() {
    final Owner owner6 = this.clinicService.findOwnerById(6).block();
    final int found = owner6.getPets().size();

    Pet pet = new Pet();
    pet.setName("bowser");
    Collection<PetType> types = this.clinicService.findPetTypes().collectList().block();
    pet.setType(EntityUtils.getById(types, PetType.class, 2));//id가 2인 petType 할당
    pet.setBirthDate(LocalDate.now());
    pet.setOwnerId(owner6.getId());
    owner6.getPets().add(pet);
//    this.clinicService.saveOwner(owner6).block();
//    assertThat(owner6.getPets().size(), is(found + 1));

//    owner6 = this.clinicService.findOwnerById(6).block();
//    assertThat(owner6.getPets().size(), is(found + 1));
//    assertThat(pet.getId(), notNullValue());

    tx.execute(status -> {
      Mono<Owner> save = clinicService.saveOwner(owner6);
      Mono<Owner> find = clinicService.findOwnerById(6);

      return save.then(find.handle((owner, sink) -> {
        if (owner.getPets().size() == found + 1) {
          status.setRollbackOnly();
        }
      }));

    }).as(StepVerifier::create).verifyComplete();
  }

  @Test
  @Order(7)
  void shouldUpdatePetName() throws Exception {
    final Pet pet7 = this.clinicService.findPetById(7).block();
    final String oldName = pet7.getName();
    final String newName = oldName + "X";
    pet7.setName(newName);
//    this.clinicService.savePet(pet7).block();
//    pet7 = this.clinicService.findPetById(7).block();
//    assertThat(pet7.getName(), equalTo(newName));

    tx.execute(status -> {
      Mono<Pet> save = clinicService.savePet(pet7);
      Mono<Pet> find = clinicService.findPetById(7);

      return save.then(find.handle((pet, synchronousSink) -> {
        if (pet.getName().equals(newName)) {
          status.setRollbackOnly();
        }
      }));
    }).as(StepVerifier::create).verifyComplete();
  }

  @Test
  @Order(8)
  void shouldFindVets() {
    Collection<Vet> vets = this.clinicService.findVets().collectList().block();

    Vet vet = EntityUtils.getById(vets, Vet.class, 3);
    assertThat(vet.getLastName(), equalTo("Douglas"));
    assertThat(vet.getSpecialties().size(), is(2));
    assertThat(vet.getSpecialties().get(0).getName(), equalTo("dentistry"));
    assertThat(vet.getSpecialties().get(1).getName(), equalTo("surgery"));
  }

  @Test
  @Order(9)
  void shouldAddNewVisitForPet() {
    final Pet pet7 = this.clinicService.findPetById(7).block();
    final int found = pet7.getVisits().size();

    Visit visit = new Visit();
    pet7.getVisits().add(visit);
    visit.setPetId(pet7.getId());
    visit.setDescription("test");

//    this.clinicService.saveVisit(visit).block();
//    this.clinicService.savePet(pet7).block();

//    pet7 = this.clinicService.findPetById(7).block();
//    assertThat(pet7.getVisits().size(), is(found + 1));
//    assertThat(visit.getId(), notNullValue());

    tx.execute(status -> {
      Mono<Visit> saveVisit = clinicService.saveVisit(visit);
      Mono<Pet> savePet = clinicService.savePet(pet7);
      Mono<Pet> find = clinicService.findPetById(7);

      return saveVisit.then(savePet.then(find.handle((pet, synchronousSink) -> {
        if (pet.getVisits().size() == found + 1) {
          status.setRollbackOnly();
        }
      })));

    }).as(StepVerifier::create).verifyComplete();
  }

  @Test
  @Order(10)
  void shouldFindVisitsByPetId() throws Exception {
    Collection<Visit> visits = this.clinicService.findVisitsByPetId(7).collectList().block();
    assertThat(visits.size(), is(2));
    Visit[] visitArr = visits.toArray(new Visit[visits.size()]);
    assertThat(visitArr[0].getPet(), notNullValue());
    assertThat(visitArr[0].getDate(), notNullValue());
    assertThat(visitArr[0].getPet().getId(), is(7));
  }

  @Test
  @Order(11)
  void shouldFindAllPets() {
    Collection<Pet> pets = this.clinicService.findAllPets().collectList().block();
    Pet pet1 = EntityUtils.getById(pets, Pet.class, 1);
    assertThat(pet1.getName(), equalTo("Leo"));
    Pet pet3 = EntityUtils.getById(pets, Pet.class, 3);
    assertThat(pet3.getName(), equalTo("Rosy"));
  }

  @Test
  @Order(12)
  void shouldFindVisitDyId() {
    Visit visit = this.clinicService.findVisitById(1).block();
    assertThat(visit.getId(), is(1));
    assertThat(visit.getPet().getName(), equalTo("Samantha"));
  }

  @Test
  @Order(13)
  void shouldFindAllVisits() {
    Collection<Visit> visits = this.clinicService.findAllVisits().collectList().block();
    Visit visit1 = EntityUtils.getById(visits, Visit.class, 1);
    assertThat(visit1.getPet().getName(), equalTo("Samantha"));
    Visit visit3 = EntityUtils.getById(visits, Visit.class, 3);
    assertThat(visit3.getPet().getName(), equalTo("Max"));
  }

  @Test
  @Order(14)
  void shouldInsertVisit() {
    final Collection<Visit> visits = this.clinicService.findAllVisits().collectList().block();
    final int found = visits.size();

    final Pet pet = this.clinicService.findPetById(1).block();

    Visit visit = new Visit();
    visit.setPetId(pet.getId());
    visit.setDate(LocalDate.now());
    visit.setDescription("new visit");

//    this.clinicService.saveVisit(visit).block();
//    assertThat(visit.getId().longValue(), not(0));

//    visits = this.clinicService.findAllVisits().collectList().block();
//    assertThat(visits.size(), is(found + 1));

    tx.execute(status -> {
      Mono<Visit> save = clinicService.saveVisit(visit);
      Mono<List<Visit>> findAll = clinicService.findAllVisits().collectList();

      return save.then(findAll.handle((visits1, synchronousSink) -> {
        if (visits1.size() == found + 1) {
          status.setRollbackOnly();
        }
      }));

    }).as(StepVerifier::create).verifyComplete();
  }

  @Test
  @Order(16)
  void shouldUpdateVisit() {
    final Visit visit = this.clinicService.findVisitById(1).block();
    final String oldDesc = visit.getDescription();
    final String newDesc = oldDesc + "X";
    visit.setDescription(newDesc);

//    this.clinicService.saveVisit(visit).block();
//    visit = this.clinicService.findVisitById(1).block();
//    assertThat(visit.getDescription(), equalTo(newDesc));

    tx.execute(status -> {
      Mono<Visit> save = clinicService.saveVisit(visit);
      Mono<Visit> find = clinicService.findVisitById(1);

      return save.then(find.handle((visit1, synchronousSink) -> {
        if (visit1.getDescription().equals(newDesc)) {
          status.setRollbackOnly();
        }
      }));

    }).as(StepVerifier::create).verifyComplete();
  }

  @Test
  @Order(17)
  void shouldFindVetDyId() {
    Vet vet = this.clinicService.findVetById(1).block();
    assertThat(vet.getFirstName(), equalTo("James"));
    assertThat(vet.getLastName(), equalTo("Carter"));
  }

  @Test
  @Order(18)
  void shouldInsertVet() {
    final Collection<Vet> vets = this.clinicService.findAllVets().collectList().block();
    final int found = vets.size();

    Vet vet = new Vet();
    vet.setFirstName("John");
    vet.setLastName("Dow");

//    this.clinicService.saveVet(vet).block();
//    assertThat(vet.getId().longValue(), not(0));

//    vets = this.clinicService.findAllVets().collectList().block();
//    assertThat(vets.size(), is(found + 1));

    tx.execute(status -> {
      Mono<Vet> save = clinicService.saveVet(vet);
      Mono<List<Vet>> findAll = clinicService.findAllVets().collectList();

      return save.then(findAll.handle((vets1, synchronousSink) -> {
        if (vets1.size() == found + 1) {
          status.setRollbackOnly();
        }
      }));

    }).as(StepVerifier::create).verifyComplete();
  }

  @Test
  @Order(19)
  void shouldUpdateVet() {
    final Vet vet = this.clinicService.findVetById(1).block();
    final String oldLastName = vet.getLastName();
    final String newLastName = oldLastName + "X";
    vet.setLastName(newLastName);

//    this.clinicService.saveVet(vet).block();
//    vet = this.clinicService.findVetById(1).block();
//    assertThat(vet.getLastName(), equalTo(newLastName));

    tx.execute(status -> {
      Mono<Vet> save = clinicService.saveVet(vet);
      Mono<Vet> find = clinicService.findVetById(1);

      return save.then(find.handle((vet1, synchronousSink) -> {
        if (vet.getLastName().equals(newLastName)) {
          status.setRollbackOnly();
        }
      }));

    }).as(StepVerifier::create).verifyComplete();
  }

  @Test
  @Order(20)
  void shouldFindAllOwners() {
    Collection<Owner> owners = this.clinicService.findAllOwners().collectList().block();
    Owner owner1 = EntityUtils.getById(owners, Owner.class, 1);
    assertThat(owner1.getFirstName(), equalTo("George"));
    Owner owner3 = EntityUtils.getById(owners, Owner.class, 3);
    assertThat(owner3.getFirstName(), equalTo("Eduardo"));
  }

  @Test
  @Order(21)
  void shouldFindPetTypeById() {
    PetType petType = this.clinicService.findPetTypeById(1).block();
    assertThat(petType.getName(), equalTo("cat"));
  }

  @Test
  @Order(22)
  void shouldFindAllPetTypes() {
    Collection<PetType> petTypes = this.clinicService.findAllPetTypes().collectList().block();
    PetType petType1 = EntityUtils.getById(petTypes, PetType.class, 1);
    assertThat(petType1.getName(), equalTo("cat"));
    PetType petType3 = EntityUtils.getById(petTypes, PetType.class, 3);
    assertThat(petType3.getName(), equalTo("lizard"));
  }

  @Test
  @Order(23)
  void shouldInsertPetType() {
    Collection<PetType> petTypes = this.clinicService.findAllPetTypes().collectList().block();
    final int found = petTypes.size();

    final PetType petType = new PetType();
    petType.setName("tiger");

//    this.clinicService.savePetType(petType).block();
//    assertThat(petType.getId().longValue(), not(0));

//    petTypes = this.clinicService.findAllPetTypes().collectList().block();
//    assertThat(petTypes.size(), is(found + 1));
    tx.execute(status -> {
      Mono<PetType> save = clinicService.savePetType(petType);
      Mono<List<PetType>> findAll = clinicService.findAllPetTypes().collectList();

      return save.then(findAll.handle((petTypes1, synchronousSink) -> {
        if (petTypes1.size() == found + 1) {
          status.setRollbackOnly();
        }
      }));
    }).as(StepVerifier::create).verifyComplete();
  }

  @Test
  @Order(24)
  void shouldUpdatePetType() {
    final PetType petType = this.clinicService.findPetTypeById(1).block();
    final String oldLastName = petType.getName();
    final String newLastName = oldLastName + "X";
    petType.setName(newLastName);
//    this.clinicService.savePetType(petType).block();
//    petType = this.clinicService.findPetTypeById(1).block();
//    assertThat(petType.getName(), equalTo(newLastName));

    tx.execute(status -> {
      Mono<PetType> save = clinicService.savePetType(petType);
      Mono<PetType> find = clinicService.findPetTypeById(1);

      return save.then(find.handle((petType1, synchronousSink) -> {
        if (petType1.getName().equals(newLastName)) {
          status.setRollbackOnly();
        }
      }));
    }).as(StepVerifier::create).verifyComplete();
  }

  @Test
  @Order(25)
  void shouldFindSpecialtyById() {
    Specialty specialty = this.clinicService.findSpecialtyById(1).block();
    assertThat(specialty.getName(), equalTo("radiology"));
  }

  @Test
  @Order(26)
  void shouldFindAllSpecialtys() {
    Collection<Specialty> specialties = this.clinicService.findAllSpecialties().collectList()
        .block();
    Specialty specialty1 = EntityUtils.getById(specialties, Specialty.class, 1);
    assertThat(specialty1.getName(), equalTo("radiology"));
    Specialty specialty3 = EntityUtils.getById(specialties, Specialty.class, 3);
    assertThat(specialty3.getName(), equalTo("dentistry"));
  }

  @Test
  @Order(27)
  void shouldInsertSpecialty() {
    Collection<Specialty> specialties = this.clinicService.findAllSpecialties().collectList()
        .block();
    final int found = specialties.size();

    final Specialty specialty = new Specialty();
    specialty.setName("dermatologist");

//    this.clinicService.saveSpecialty(specialty).block();
//    assertThat(specialty.getId().longValue(), not(0));

//    specialties = this.clinicService.findAllSpecialties().collectList().block();
//    assertThat(specialties.size(), is(found + 1));

    tx.execute(status -> {
      Mono<Specialty> save = clinicService.saveSpecialty(specialty);
      Mono<List<Specialty>> find = clinicService.findAllSpecialties().collectList();

      return save.then(find.handle((specialties1, synchronousSink) -> {
        if (specialties1.size() == found + 1) {
          status.setRollbackOnly();
        }
      }));
    }).as(StepVerifier::create).verifyComplete();
  }

  @Test
  @Order(28)
  void shouldUpdateSpecialty() {
    Specialty specialty = this.clinicService.findSpecialtyById(1).block();
    String oldLastName = specialty.getName();
    final String newLastName = oldLastName + "X";
    specialty.setName(newLastName);
//    this.clinicService.saveSpecialty(specialty).block();
//    specialty = this.clinicService.findSpecialtyById(1).block();
//    assertThat(specialty.getName(), equalTo(newLastName));

    tx.execute(status -> {
      Mono<Specialty> save = clinicService.saveSpecialty(specialty);
      Mono<Specialty> find = clinicService.findSpecialtyById(1);

      return save.then(find.handle((specialty1, synchronousSink) -> {
        if (specialty1.getName().equals(newLastName)) {
          status.setRollbackOnly();
        }
      }));
    }).as(StepVerifier::create).verifyComplete();
  }

  @Test
  @Order(29)
  void shouldDeleteSpecialty() {
    Specialty specialty = new Specialty();
    specialty.setName("test");
    this.clinicService.saveSpecialty(specialty).block();
    Integer specialtyId = specialty.getId();
    assertThat(specialtyId, notNullValue());
    specialty = this.clinicService.findSpecialtyById(specialtyId).block();
    assertThat(specialty, notNullValue());
    this.clinicService.deleteSpecialty(specialty).block();
    specialty = this.clinicService.findSpecialtyById(specialtyId).block();
    assertThat(specialty, nullValue());
  }

  @Test
  @Order(30)
  void shouldDeleteVet() {
    Vet vet = this.clinicService.findVetById(1).block();
    this.clinicService.deleteVet(vet).block();
    vet = this.clinicService.findVetById(1).block();
    assertThat(vet, nullValue());
  }

  @Test
  @Order(31)
  void shouldDeletePet() {
    Pet pet = this.clinicService.findPetById(1).block();
    this.clinicService.deletePet(pet).block();
    Pet pet1 = this.clinicService.findPetById(1).block();
    assertThat(pet1, nullValue());

  }

  @Test
  @Order(32)
  void shouldDeletePetType() {
    PetType petType = this.clinicService.findPetTypeById(1).block();
    this.clinicService.deletePetType(petType).block();
    petType = this.clinicService.findPetTypeById(1).block();
    assertThat(petType, nullValue());
  }

  @Test
  @Order(33)
  void shouldDeleteOwner() {
    Owner owner = this.clinicService.findOwnerById(1).block();
    this.clinicService.deleteOwner(owner).block();
    owner = this.clinicService.findOwnerById(1).block();
    assertThat(owner, nullValue());
  }
}
