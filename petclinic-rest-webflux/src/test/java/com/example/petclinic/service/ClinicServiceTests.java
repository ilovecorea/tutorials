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
import com.example.petclinic.model.Specialty;
import com.example.petclinic.model.Vet;
import com.example.petclinic.model.Visit;
import com.example.petclinic.util.EntityUtils;
import java.time.LocalDate;
import java.util.Collection;
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

  /**
   * StepVerifier를 통한 검증
   */
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

  /**
   * StepVerifier를 통한 검증
   */
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
    owner = clinicService.saveOwner(owner).block();
    assertThat(owner.getId().longValue(), not(0));
    assertThat(owner.getPets(), nullValue());
    owners = clinicService.findOwnerByLastName("Schultz").collectList().block();
    assertThat(owners.size(), is(found + 1));
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

  @Test
  void shouldInsertPetIntoDatabaseAndGenerateId() {
    Owner owner6 = this.clinicService.findOwnerById(6).block();
    int found = owner6.getPets().size();

    Pet pet = new Pet();
    pet.setName("bowser");
    Collection<PetType> types = this.clinicService.findPetTypes().collectList().block();
    pet.setType(EntityUtils.getById(types, PetType.class, 2));
    pet.setBirthDate(LocalDate.now());
    pet.setOwnerId(owner6.getId());
    owner6.addPet(pet);
    this.clinicService.saveOwner(owner6).block();
    assertThat(owner6.getPets().size(), is(found + 1));

    owner6 = this.clinicService.findOwnerById(6).block();
    assertThat(owner6.getPets().size(), is(found + 1));
    assertThat(pet.getId(), notNullValue());
  }

  @Test
  void shouldUpdatePetName() throws Exception {
    Pet pet7 = this.clinicService.findPetById(7).block();
    String oldName = pet7.getName();

    String newName = oldName + "X";
    pet7.setName(newName);
    this.clinicService.savePet(pet7).block();

    pet7 = this.clinicService.findPetById(7).block();
    assertThat(pet7.getName(), equalTo(newName));
  }

  @Test
  void shouldFindVets() {
    Collection<Vet> vets = this.clinicService.findVets().collectList().block();

    Vet vet = EntityUtils.getById(vets, Vet.class, 3);
    assertThat(vet.getLastName(), equalTo("Douglas"));
    assertThat(vet.getSpecialties().size(), is(2));
    assertThat(vet.getSpecialties().get(0).getName(), equalTo("dentistry"));
    assertThat(vet.getSpecialties().get(1).getName(), equalTo("surgery"));
  }

  @Test
  void shouldAddNewVisitForPet() {
    Pet pet7 = this.clinicService.findPetById(7).block();
    int found = pet7.getVisits().size();
    Visit visit = new Visit();
    pet7.addVisit(visit);
    visit.setPetId(pet7.getId());
    visit.setDescription("test");
    this.clinicService.saveVisit(visit).block();
    this.clinicService.savePet(pet7).block();

    pet7 = this.clinicService.findPetById(7).block();
    assertThat(pet7.getVisits().size(), is(found + 1));
    assertThat(visit.getId(), notNullValue());
  }

  @Test
  void shouldFindVisitsByPetId() throws Exception {
    Collection<Visit> visits = this.clinicService.findVisitsByPetId(7).collectList().block();
    assertThat(visits.size(), is(2));
    Visit[] visitArr = visits.toArray(new Visit[visits.size()]);
    assertThat(visitArr[0].getPet(), notNullValue());
    assertThat(visitArr[0].getDate(), notNullValue());
    assertThat(visitArr[0].getPet().getId(), is(7));
  }

  @Test
  void shouldFindAllPets(){
    Collection<Pet> pets = this.clinicService.findAllPets().collectList().block();
    Pet pet1 = EntityUtils.getById(pets, Pet.class, 1);
    assertThat(pet1.getName(), equalTo("Leo"));
    Pet pet3 = EntityUtils.getById(pets, Pet.class, 3);
    assertThat(pet3.getName(), equalTo("Rosy"));
  }

  @Test
  void shouldDeletePet(){
    Pet pet = this.clinicService.findPetById(1).block();
    this.clinicService.deletePet(pet).block();
    try {
      pet = this.clinicService.findPetById(1).block();
    } catch (Exception e) {
      pet = null;
    }
    assertThat(pet, nullValue());
  }

  @Test
  void shouldFindVisitDyId(){
    Visit visit = this.clinicService.findVisitById(1).block();
    assertThat(visit.getId(), is(1));
    assertThat(visit.getPet().getName(), equalTo("Samantha"));
  }

  @Test
  void shouldFindAllVisits(){
    Collection<Visit> visits = this.clinicService.findAllVisits().collectList().block();
    Visit visit1 = EntityUtils.getById(visits, Visit.class, 1);
    assertThat(visit1.getPet().getName(), equalTo("Samantha"));
    Visit visit3 = EntityUtils.getById(visits, Visit.class, 3);
    assertThat(visit3.getPet().getName(), equalTo("Max"));
  }

  @Test
  void shouldInsertVisit() {
    Collection<Visit> visits = this.clinicService.findAllVisits().collectList().block();
    int found = visits.size();

    Pet pet = this.clinicService.findPetById(1).block();

    Visit visit = new Visit();
    visit.setPetId(pet.getId());
    visit.setDate(LocalDate.now());
    visit.setDescription("new visit");

    this.clinicService.saveVisit(visit).block();
    assertThat(visit.getId().longValue(), not(0));

    visits = this.clinicService.findAllVisits().collectList().block();
    assertThat(visits.size(), is(found + 1));
  }

  @Test
  void shouldUpdateVisit(){
    Visit visit = this.clinicService.findVisitById(1).block();
    String oldDesc = visit.getDescription();
    String newDesc = oldDesc + "X";
    visit.setDescription(newDesc);
    this.clinicService.saveVisit(visit).block();
    visit = this.clinicService.findVisitById(1).block();
    assertThat(visit.getDescription(), equalTo(newDesc));
  }

  @Test
  void shouldFindVetDyId(){
    Vet vet = this.clinicService.findVetById(1).block();
    assertThat(vet.getFirstName(), equalTo("James"));
    assertThat(vet.getLastName(), equalTo("Carter"));
  }

  @Test
  void shouldInsertVet() {
    Collection<Vet> vets = this.clinicService.findAllVets().collectList().block();
    int found = vets.size();

    Vet vet = new Vet();
    vet.setFirstName("John");
    vet.setLastName("Dow");

    this.clinicService.saveVet(vet).block();
    assertThat(vet.getId().longValue(), not(0));

    vets = this.clinicService.findAllVets().collectList().block();
    assertThat(vets.size(), is(found + 1));
  }

  @Test
  void shouldUpdateVet(){
    Vet vet = this.clinicService.findVetById(1).block();
    String oldLastName = vet.getLastName();
    String newLastName = oldLastName + "X";
    vet.setLastName(newLastName);
    this.clinicService.saveVet(vet).block();
    vet = this.clinicService.findVetById(1).block();
    assertThat(vet.getLastName(), equalTo(newLastName));
  }

  @Test
  void shouldDeleteVet(){
    Vet vet = this.clinicService.findVetById(1).block();
    this.clinicService.deleteVet(vet).block();
    try {
      vet = this.clinicService.findVetById(1).block();
    } catch (Exception e) {
      vet = null;
    }
    assertThat(vet, nullValue());
  }

  @Test
  void shouldFindAllOwners(){
    Collection<Owner> owners = this.clinicService.findAllOwners().collectList().block();
    Owner owner1 = EntityUtils.getById(owners, Owner.class, 1);
    assertThat(owner1.getFirstName(), equalTo("George"));
    Owner owner3 = EntityUtils.getById(owners, Owner.class, 3);
    assertThat(owner3.getFirstName(), equalTo("Eduardo"));
  }

  @Test
  void shouldDeleteOwner(){
    Owner owner = this.clinicService.findOwnerById(1).block();
    this.clinicService.deleteOwner(owner).block();
    try {
      owner = this.clinicService.findOwnerById(1).block();
    } catch (Exception e) {
      owner = null;
    }
    assertThat(owner, nullValue());
  }

  @Test
  void shouldFindPetTypeById() {
    PetType petType = this.clinicService.findPetTypeById(1).block();
    assertThat(petType.getName(), equalTo("cat"));
  }

  @Test
  void shouldFindAllPetTypes(){
    Collection<PetType> petTypes = this.clinicService.findAllPetTypes().collectList().block();
    PetType petType1 = EntityUtils.getById(petTypes, PetType.class, 1);
    assertThat(petType1.getName(), equalTo("cat"));
    PetType petType3 = EntityUtils.getById(petTypes, PetType.class, 3);
    assertThat(petType3.getName(), equalTo("lizard"));
  }

  @Test
  void shouldInsertPetType() {
    Collection<PetType> petTypes = this.clinicService.findAllPetTypes().collectList().block();
    int found = petTypes.size();

    PetType petType = new PetType();
    petType.setName("tiger");

    this.clinicService.savePetType(petType).block();
    assertThat(petType.getId().longValue(), not(0));

    petTypes = this.clinicService.findAllPetTypes().collectList().block();
    assertThat(petTypes.size(), is(found + 1));
  }

  @Test
  void shouldUpdatePetType(){
    PetType petType = this.clinicService.findPetTypeById(1).block();
    String oldLastName = petType.getName();
    String newLastName = oldLastName + "X";
    petType.setName(newLastName);
    this.clinicService.savePetType(petType).block();
    petType = this.clinicService.findPetTypeById(1).block();
    assertThat(petType.getName(), equalTo(newLastName));
  }

  @Test
  void shouldDeletePetType(){
    PetType petType = this.clinicService.findPetTypeById(1).block();
    this.clinicService.deletePetType(petType).block();
    try {
      petType = this.clinicService.findPetTypeById(1).block();
    } catch (Exception e) {
      petType = null;
    }
    assertThat(petType, nullValue());
  }

  @Test
  void shouldFindSpecialtyById(){
    Specialty specialty = this.clinicService.findSpecialtyById(1).block();
    assertThat(specialty.getName(), equalTo("radiology"));
  }

  @Test
  void shouldFindAllSpecialtys(){
    Collection<Specialty> specialties = this.clinicService.findAllSpecialties().collectList().block();
    Specialty specialty1 = EntityUtils.getById(specialties, Specialty.class, 1);
    assertThat(specialty1.getName(), equalTo("radiology"));
    Specialty specialty3 = EntityUtils.getById(specialties, Specialty.class, 3);
    assertThat(specialty3.getName(), equalTo("dentistry"));
  }

  @Test
  void shouldInsertSpecialty() {
    Collection<Specialty> specialties = this.clinicService.findAllSpecialties().collectList().block();
    int found = specialties.size();

    Specialty specialty = new Specialty();
    specialty.setName("dermatologist");

    this.clinicService.saveSpecialty(specialty).block();
    assertThat(specialty.getId().longValue(), not(0));

    specialties = this.clinicService.findAllSpecialties().collectList().block();
    assertThat(specialties.size(), is(found + 1));
  }

  @Test
  void shouldUpdateSpecialty(){
    Specialty specialty = this.clinicService.findSpecialtyById(1).block();
    String oldLastName = specialty.getName();
    String newLastName = oldLastName + "X";
    specialty.setName(newLastName);
    this.clinicService.saveSpecialty(specialty).block();
    specialty = this.clinicService.findSpecialtyById(1).block();
    assertThat(specialty.getName(), equalTo(newLastName));
  }

  @Test
  void shouldDeleteSpecialty(){
    Specialty specialty = new Specialty();
    specialty.setName("test");
    this.clinicService.saveSpecialty(specialty).block();
    Integer specialtyId = specialty.getId();
    assertThat(specialtyId, notNullValue());
    specialty = this.clinicService.findSpecialtyById(specialtyId).block();
    assertThat(specialty, notNullValue());
    this.clinicService.deleteSpecialty(specialty).block();
    try {
      specialty = this.clinicService.findSpecialtyById(specialtyId).block();
    } catch (Exception e) {
      specialty = null;
    }
    assertThat(specialty, nullValue());
  }
}
