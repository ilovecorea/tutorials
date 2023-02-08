package com.example.petclinic.rest.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.example.petclinic.mapper.OwnerMapper;
import com.example.petclinic.mapper.OwnerMapperImpl;
import com.example.petclinic.mapper.PetMapper;
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
import java.text.SimpleDateFormat;
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
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
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

  @Autowired
  private PetMapper petMapper;

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
        .expectStatus().isNotFound()
        .expectBody()
        .consumeWith(System.out::println);
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
        .expectStatus().isNotFound()
        .expectBody()
        .consumeWith(System.out::println);
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
        .expectStatus().isNotFound()
        .expectBody()
        .consumeWith(System.out::println);
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
        .accept(MediaType.APPLICATION_JSON)
        .contentType(MediaType.APPLICATION_JSON)
        .body(Mono.just(newOwnerAsJSON), String.class)
        .exchange()
        .expectStatus().isCreated()
        .expectBody()
        .consumeWith(System.out::println);
  }

  @Test
  @WithMockUser(roles = "OWNER_ADMIN")
  void testCreateOwnerError() throws Exception {
    OwnerDto newOwnerDto = owners.get(0);
    newOwnerDto.setId(null);
    newOwnerDto.setFirstName(null);
    ObjectMapper mapper = new ObjectMapper();
    mapper.registerModule(new JavaTimeModule());
    mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    String newOwnerAsJSON = mapper.writeValueAsString(newOwnerDto);
    webTestClient.post().uri("/api/owners/")
        .accept(MediaType.APPLICATION_JSON)
        .contentType(MediaType.APPLICATION_JSON)
        .body(Mono.just(newOwnerAsJSON), String.class)
        .exchange()
        .expectStatus().isBadRequest()
        .expectBody()
        .consumeWith(System.out::println);
  }

  @Test
  @WithMockUser(roles = "OWNER_ADMIN")
  void testUpdateOwnerSuccess() throws Exception {
    given(this.clinicService.findOwnerById(1))
        .willReturn(Mono.just(ownerMapper.toOwner(owners.get(0))));

    int ownerId = owners.get(0).getId();
    OwnerDto updatedOwnerDto = new OwnerDto();
    updatedOwnerDto.setId(ownerId);
    updatedOwnerDto.setFirstName("GeorgeI");
    updatedOwnerDto.setLastName("Franklin");
    updatedOwnerDto.setAddress("110 W. Liberty St.");
    updatedOwnerDto.setCity("Madison");
    updatedOwnerDto.setTelephone("6085551023");
    ObjectMapper mapper = new ObjectMapper();
    mapper.registerModule(new JavaTimeModule());
    mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    String newOwnerAsJSON = mapper.writeValueAsString(updatedOwnerDto);
    webTestClient.put().uri("/api/owners/" + ownerId)
        .accept(MediaType.APPLICATION_JSON)
        .contentType(MediaType.APPLICATION_JSON)
        .body(Mono.just(newOwnerAsJSON), String.class)
        .exchange()
        .expectStatus().isNoContent();

    webTestClient.get().uri("/api/owners/" + ownerId)
        .accept(MediaType.APPLICATION_JSON)
        .exchange()
        .expectStatus().isOk()
        .expectHeader().contentType(MediaType.APPLICATION_JSON_VALUE)
        .expectBody()
        .jsonPath("$.id").isEqualTo(ownerId)
        .jsonPath("$.firstName").isEqualTo("GeorgeI")
        .consumeWith(System.out::println);

  }

  @Test
  @WithMockUser(roles = "OWNER_ADMIN")
  void testUpdateOwnerSuccessNoBodyId() throws Exception {
    given(this.clinicService.findOwnerById(1))
        .willReturn(Mono.just(ownerMapper.toOwner(owners.get(0))));
    int ownerId = owners.get(0).getId();
    OwnerDto updatedOwnerDto = new OwnerDto();
    updatedOwnerDto.setFirstName("GeorgeI");
    updatedOwnerDto.setLastName("Franklin");
    updatedOwnerDto.setAddress("110 W. Liberty St.");
    updatedOwnerDto.setCity("Madison");

    updatedOwnerDto.setTelephone("6085551023");
    ObjectMapper mapper = new ObjectMapper();
    String newOwnerAsJSON = mapper.writeValueAsString(updatedOwnerDto);
    webTestClient.put().uri("/api/owners/" + ownerId)
        .accept(MediaType.APPLICATION_JSON)
        .contentType(MediaType.APPLICATION_JSON)
        .body(Mono.just(newOwnerAsJSON), String.class)
        .exchange()
        .expectStatus().isNoContent();

    webTestClient.get().uri("/api/owners/" + ownerId)
        .accept(MediaType.APPLICATION_JSON)
        .exchange()
        .expectStatus().isOk()
        .expectBody()
        .jsonPath("$.id").isEqualTo(ownerId)
        .jsonPath("$.firstName").isEqualTo("GeorgeI")
        .consumeWith(System.out::println);
  }

  @Test
  @WithMockUser(roles = "OWNER_ADMIN")
  void testUpdateOwnerError() throws Exception {
    given(this.clinicService.findOwnerById(1))
        .willReturn(Mono.just(ownerMapper.toOwner(owners.get(0))));

    OwnerDto newOwnerDto = owners.get(0);
    newOwnerDto.setFirstName("");
    ObjectMapper mapper = new ObjectMapper();
    mapper.registerModule(new JavaTimeModule());
    String newOwnerAsJSON = mapper.writeValueAsString(newOwnerDto);
    webTestClient.put().uri("/api/owners/1")
        .accept(MediaType.APPLICATION_JSON)
        .contentType(MediaType.APPLICATION_JSON)
        .body(Mono.just(newOwnerAsJSON), String.class)
        .exchange()
        .expectStatus().isBadRequest()
        .expectBody()
        .consumeWith(System.out::println);
  }

  @Test
  @WithMockUser(roles = "OWNER_ADMIN")
  void testDeleteOwnerSuccess() throws Exception {
    OwnerDto newOwnerDto = owners.get(0);
    ObjectMapper mapper = new ObjectMapper();
    mapper.registerModule(new JavaTimeModule());
    String newOwnerAsJSON = mapper.writeValueAsString(newOwnerDto);
    final Owner owner = ownerMapper.toOwner(owners.get(0));
    given(this.clinicService.findOwnerById(1))
        .willReturn(Mono.just(owner));
    webTestClient.delete().uri("/api/owners/1")
        .accept(MediaType.APPLICATION_JSON)
        .exchange()
        .expectStatus().isNoContent()
        .expectBody()
        .consumeWith(System.out::println);
  }

  @Test
  @WithMockUser(roles = "OWNER_ADMIN")
  void testDeleteOwnerError() throws Exception {
    OwnerDto newOwnerDto = owners.get(0);
    ObjectMapper mapper = new ObjectMapper();
    mapper.registerModule(new JavaTimeModule());
    String newOwnerAsJSON = mapper.writeValueAsString(newOwnerDto);
    given(this.clinicService.findOwnerById(999))
        .willReturn(Mono.empty());
    webTestClient.delete().uri("/api/owners/999")
        .exchange()
        .expectStatus().isNotFound()
        .expectBody()
        .consumeWith(System.out::println);
  }

  @Test
  @WithMockUser(roles = "OWNER_ADMIN")
  void testCreatePetSuccess() throws Exception {
    PetDto newPet = pets.get(0);
    newPet.setId(999);
    ObjectMapper mapper = new ObjectMapper();
    mapper.registerModule(new JavaTimeModule());
    mapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd"));
    mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    String newPetAsJSON = mapper.writeValueAsString(newPet);
    log.debug("newPetAsJSON:{}", newPetAsJSON);
    given(this.clinicService.savePet(any()))
        .willReturn(Mono.just(petMapper.toPet(newPet)));
    webTestClient.post().uri("/api/owners/1/pets/")
        .accept(MediaType.APPLICATION_JSON)
        .contentType(MediaType.APPLICATION_JSON)
        .body(Mono.just(newPetAsJSON), String.class)
        .exchange()
        .expectStatus().isCreated()
        .expectBody()
        .consumeWith(System.out::println);
  }

  @Test
  @WithMockUser(roles = "OWNER_ADMIN")
  void testCreatePetError() throws Exception {
    PetDto newPet = pets.get(0);
    newPet.setId(null);
    newPet.setName(null);
    ObjectMapper mapper = new ObjectMapper();
    mapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd"));
    mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    mapper.registerModule(new JavaTimeModule());
    String newPetAsJSON = mapper.writeValueAsString(newPet);

    webTestClient.post().uri("/api/owners/1/pets/")
        .accept(MediaType.APPLICATION_JSON)
        .contentType(MediaType.APPLICATION_JSON)
        .body(Mono.just(newPetAsJSON), String.class)
        .exchange()
        .expectStatus().isBadRequest()
        .expectBody()
        .consumeWith(System.out::println);
  }

  @Test
  @WithMockUser(roles="OWNER_ADMIN")
  void testCreateVisitSuccess() throws Exception {
    VisitDto newVisit = visits.get(0);
    newVisit.setId(999);
    ObjectMapper mapper = new ObjectMapper();
    mapper.registerModule(new JavaTimeModule());
    mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    String newVisitAsJSON = mapper.writeValueAsString(visitMapper.toVisit(newVisit));
    log.debug("newVisitAsJSON:{}", newVisitAsJSON);
    webTestClient.post().uri("/api/owners/1/pets/1/visits")
        .accept(MediaType.APPLICATION_JSON)
        .contentType(MediaType.APPLICATION_JSON)
        .body(Mono.just(newVisitAsJSON), String.class)
        .exchange()
        .expectStatus().isCreated()
        .expectBody()
        .consumeWith(System.out::println);
  }
}
