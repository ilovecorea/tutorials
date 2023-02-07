package com.example.petclinic.rest.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

import com.example.petclinic.mapper.OwnerMapper;
import com.example.petclinic.mapper.OwnerMapperImpl;
import com.example.petclinic.mapper.PetMapperImpl;
import com.example.petclinic.mapper.VisitMapper;
import com.example.petclinic.mapper.VisitMapperImpl;
import com.example.petclinic.model.Owner;
import com.example.petclinic.rest.advice.GlobalErrorAttributes;
import com.example.petclinic.rest.advice.GlobalErrorExceptionHandler;
import com.example.petclinic.rest.dto.OwnerDto;
import com.example.petclinic.rest.dto.PetDto;
import com.example.petclinic.rest.dto.PetTypeDto;
import com.example.petclinic.rest.dto.VisitDto;
import com.example.petclinic.service.ClinicService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@ExtendWith(SpringExtension.class)
@WebFluxTest(OwnerRestController.class)
@ContextConfiguration(classes = {
    ApplicationTestConfig.class,
    OwnerMapperImpl.class,
    VisitMapperImpl.class,
    PetMapperImpl.class
})
@Import(value = {
    GlobalErrorAttributes.class,
    GlobalErrorExceptionHandler.class,
    OwnerRestController.class
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

  private static final Logger log = LoggerFactory.getLogger(OwnerRestControllerTests.class);

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
        .willReturn(Mono.just(ownerMapper.toOwner(owners.get(0))));
    webTestClient.get().uri("/api/owners/1")
        .accept(MediaType.APPLICATION_JSON)
        .exchange()
        .expectStatus().isOk()
        .expectHeader().contentType(MediaType.APPLICATION_JSON_VALUE)
        .expectBody()
        .jsonPath("$.id").isEqualTo(1)
        .jsonPath("$.firstName").isEqualTo("George")
        .consumeWith(System.out::println);
  }

  @Test
  @WithMockUser(roles = "OWNER_ADMIN")
  public void testGetOwnerNotFound() {
    given(this.clinicService.findOwnerById(2)).willReturn(Mono.empty());
    webTestClient.get().uri("/api/owners/2")
        .accept(MediaType.APPLICATION_JSON)
        .exchange()
        .expectStatus().isNotFound();
  }

  @Test
  @WithMockUser(roles = "OWNER_ADMIN")
  void testGetOwnersListSuccess() {
    owners.remove(0);
    owners.remove(1);
    given(this.clinicService.findOwnerByLastName("Davis"))
        .willReturn(Flux.fromIterable(ownerMapper.toOwners(owners)));

    webTestClient.get().uri("/api/owners?lastName=Davis")
        .accept(MediaType.APPLICATION_JSON)
        .exchange()
        .expectStatus().isOk()
        .expectHeader().contentType(MediaType.APPLICATION_JSON_VALUE)
        .expectBody()
        .jsonPath("$.[0].id").isEqualTo(2)
        .jsonPath("$.[0].firstName").isEqualTo("Betty")
        .jsonPath("$.[1].id").isEqualTo(4)
        .jsonPath("$.[1].firstName").isEqualTo("Harold")
        .consumeWith(System.out::println);
  }

  @Test
  @WithMockUser(roles = "OWNER_ADMIN")
  void testGetOwnersListNotFound() throws Exception {
    owners.clear();
    given(this.clinicService.findOwnerByLastName("0"))
        .willReturn(Flux.fromIterable(ownerMapper.toOwners(owners)));
    webTestClient.get().uri("/api/owners/?lastName=0")
        .accept(MediaType.APPLICATION_JSON)
        .exchange()
        .expectStatus().isNotFound();
  }

  @Test
  @WithMockUser(roles = "OWNER_ADMIN")
  void testGetAllOwnersSuccess() throws Exception {
    owners.remove(0);
    owners.remove(1);
    given(this.clinicService.findAllOwners())
        .willReturn(Flux.fromIterable(ownerMapper.toOwners(owners)));
    webTestClient.get().uri("/api/owners/")
        .accept(MediaType.APPLICATION_JSON)
        .exchange()
        .expectStatus().isOk()
        .expectHeader().contentType(MediaType.APPLICATION_JSON_VALUE)
        .expectBody()
        .jsonPath("$.[0].id").isEqualTo(2)
        .jsonPath("$.[0].firstName").isEqualTo("Betty")
        .jsonPath("$.[1].id").isEqualTo(4)
        .jsonPath("$.[1].firstName").isEqualTo("Harold")
        .consumeWith(System.out::println);
  }

  @Test
  @WithMockUser(roles = "OWNER_ADMIN")
  void testGetAllOwnersNotFound() throws Exception {
    owners.clear();
    given(this.clinicService.findAllOwners())
        .willReturn(Flux.fromIterable(ownerMapper.toOwners(owners)));
    webTestClient.get().uri("/api/owners/")
        .accept(MediaType.APPLICATION_JSON)
        .exchange()
        .expectStatus().isNotFound();
  }

  @Test
  @WithMockUser(roles = "OWNER_ADMIN")
  void testCreateOwnerSuccess() throws Exception {
    OwnerDto newOwnerDto = owners.get(0);
    newOwnerDto.setId(null);
    Owner result = ownerMapper.toOwner(newOwnerDto);
    result.setId(5);
    given(this.clinicService.saveOwner(any()))
        .willReturn(Mono.just(result));

    ObjectMapper mapper = new ObjectMapper();
    mapper.registerModule(new JavaTimeModule());
    mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    String newOwnerAsJSON = mapper.writeValueAsString(newOwnerDto);
    webTestClient.post().uri("/api/owners/")
        .contentType(MediaType.APPLICATION_JSON)
        .accept(MediaType.APPLICATION_JSON)
        .contentType(MediaType.APPLICATION_JSON)
        .body(Mono.just(newOwnerAsJSON), String.class)
        .exchange()
        .expectStatus().isCreated();
  }
}
