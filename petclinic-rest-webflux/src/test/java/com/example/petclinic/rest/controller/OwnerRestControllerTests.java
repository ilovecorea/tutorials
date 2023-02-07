package com.example.petclinic.rest.controller;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

import com.example.petclinic.mapper.OwnerMapper;
import com.example.petclinic.mapper.OwnerMapperImpl;
import com.example.petclinic.mapper.VisitMapper;
import com.example.petclinic.mapper.VisitMapperImpl;
import com.example.petclinic.model.Visit;
import com.example.petclinic.rest.dto.OwnerDto;
import com.example.petclinic.rest.dto.PetDto;
import com.example.petclinic.rest.dto.PetTypeDto;
import com.example.petclinic.rest.dto.VisitDto;
import com.example.petclinic.service.ClinicService;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

@ExtendWith(SpringExtension.class)
@WebFluxTest(OwnerRestController.class)
@ContextConfiguration(classes = {
    OwnerMapperImpl.class, VisitMapperImpl.class
})
public class OwnerRestControllerTests {

  @Autowired
  WebTestClient webTestClient;

  @Autowired
  private OwnerMapper ownerMapper;

  @Autowired
  private VisitMapper visitMapper;

  @MockBean
  private ClinicService clinicService;

  private List<OwnerDto> owners;

  private List<PetDto> pets;

  private List<VisitDto> visits;

  @BeforeEach
  void initOwners() {
    owners = new ArrayList<>();

    OwnerDto ownerWithPet = new OwnerDto();
    owners.add(
        ownerWithPet.id(1).firstName("George").lastName("Franklin").address("110 W. Liberty St.")
            .city("Madison").telephone("6085551023")
            .addPetsItem(getTestPetWithIdAndName(ownerWithPet, 1, "Rosy")));
    OwnerDto owner = new OwnerDto();
    owners.add(owner.id(2).firstName("Betty").lastName("Davis").address("638 Cardinal Ave.")
        .city("Sun Prairie").telephone("6085551749"));
    owner = new OwnerDto();
    owners.add(owner.id(3).firstName("Eduardo").lastName("Rodriquez").address("2693 Commerce St.")
        .city("McFarland").telephone("6085558763"));
    owner = new OwnerDto();
    owners.add(owner.id(4).firstName("Harold").lastName("Davis").address("563 Friendly St.")
        .city("Windsor").telephone("6085553198"));

    PetTypeDto petType = new PetTypeDto();
    petType.id(2)
        .name("dog");

    pets = new ArrayList<>();
    PetDto pet = new PetDto();
    pets.add(pet.id(3)
        .name("Rosy")
        .birthDate(LocalDate.now())
        .type(petType));

    pet = new PetDto();
    pets.add(pet.id(4)
        .name("Jewel")
        .birthDate(LocalDate.now())
        .type(petType));

    visits = new ArrayList<>();
    VisitDto visit = new VisitDto();
    visit.setId(2);
    visit.setPetId(pet.getId());
    visit.setDate(LocalDate.now());
    visit.setDescription("rabies shot");
    visits.add(visit);

    visit = new VisitDto();
    visit.setId(3);
    visit.setPetId(pet.getId());
    visit.setDate(LocalDate.now());
    visit.setDescription("neutered");
    visits.add(visit);
  }

  private PetDto getTestPetWithIdAndName(final OwnerDto owner, final int id, final String name) {
    PetTypeDto petType = new PetTypeDto();
    PetDto pet = new PetDto();
    pet.id(id).name(name).birthDate(LocalDate.now()).type(petType.id(2).name("dog"))
        .addVisitsItem(getTestVisitForPet(pet, 1));
    return pet;
  }

  private VisitDto getTestVisitForPet(final PetDto pet, final int id) {
    VisitDto visit = new VisitDto();
    return visit.id(id).date(LocalDate.now()).description("test" + id);
  }

  @Test
  @WithMockUser(roles = "OWNER_ADMIN")
  public void testGetOwnerSuccess() {
    given(this.clinicService.findOwnerById(1))
        .willReturn(Mono.just(ownerMapper.toModel(owners.get(0))));
    webTestClient.get().uri("/api/owners/1")
        .header(HttpHeaders.ACCEPT, "application/json")
        .exchange()
        .expectStatus().isOk()
        .expectHeader().contentType("application/json")
        .expectBody()
        .jsonPath("$.id").isEqualTo(1)
        .jsonPath("$.firstName").isEqualTo("George");
  }
}
