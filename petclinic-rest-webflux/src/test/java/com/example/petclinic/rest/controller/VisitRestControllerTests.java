package com.example.petclinic.rest.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

import com.example.petclinic.mapper.VisitMapper;
import com.example.petclinic.mapper.VisitMapperImpl;
import com.example.petclinic.model.Owner;
import com.example.petclinic.model.Pet;
import com.example.petclinic.model.PetType;
import com.example.petclinic.model.Visit;
import com.example.petclinic.rest.advice.GlobalErrorAttributes;
import com.example.petclinic.rest.advice.GlobalErrorExceptionHandler;
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
@WebFluxTest(VisitRestController.class)
@ContextConfiguration(classes = {
    ApplicationTestConfig.class
})
@Import(value = {
    GlobalErrorAttributes.class,
    GlobalErrorExceptionHandler.class,
    VisitMapperImpl.class
})
public class VisitRestControllerTests {

  @MockBean
  protected ClinicService clinicService;

  @Autowired
  private VisitMapper visitMapper;

  @Autowired
  WebTestClient webTestClient;

  private List<Visit> visits;

  @BeforeEach
  void initVisits() {

    visits = new ArrayList<>();

    Owner owner = new Owner();
    owner.setId(1);
    owner.setFirstName("Eduardo");
    owner.setLastName("Rodriquez");
    owner.setAddress("2693 Commerce St.");
    owner.setCity("McFarland");
    owner.setTelephone("6085558763");

    PetType petType = new PetType();
    petType.setId(2);
    petType.setName("dog");

    Pet pet = new Pet();
    pet.setId(8);
    pet.setName("Rosy");
    pet.setBirthDate(LocalDate.now());
    pet.setOwner(owner);
    pet.setType(petType);


    Visit visit = new Visit();
    visit.setId(2);
    visit.setPet(pet);
    visit.setDate(LocalDate.now());
    visit.setDescription("rabies shot");
    visits.add(visit);

    visit = new Visit();
    visit.setId(3);
    visit.setPet(pet);
    visit.setDate(LocalDate.now());
    visit.setDescription("neutered");
    visits.add(visit);

  }

  @Test
  @WithMockUser(roles="OWNER_ADMIN")
  void testGetVisitSuccess() throws Exception {
    given(this.clinicService.findVisitById(2)).willReturn(Mono.just(visits.get(0)));
    webTestClient.get().uri("/api/visits/2")
        .accept(MediaType.APPLICATION_JSON)
        .exchange()
        .expectStatus().isOk()
        .expectHeader().contentType(MediaType.APPLICATION_JSON)
        .expectBody()
        .jsonPath("$.id").isEqualTo(2)
        .jsonPath("$.description").isEqualTo("rabies shot");
  }

  @Test
  @WithMockUser(roles="OWNER_ADMIN")
  void testGetVisitNotFound() throws Exception {
    given(this.clinicService.findVisitById(999)).willReturn(Mono.empty());
    webTestClient.get().uri("/api/visits/999")
        .accept(MediaType.APPLICATION_JSON)
        .exchange()
        .expectStatus().isNotFound();
  }

  @Test
  @WithMockUser(roles="OWNER_ADMIN")
  void testGetAllVisitsSuccess() throws Exception {
    given(this.clinicService.findAllVisits()).willReturn(Flux.fromIterable(visits));
    webTestClient.get().uri("/api/visits/")
        .accept(MediaType.APPLICATION_JSON)
        .exchange()
        .expectStatus().isOk()
        .expectHeader().contentType(MediaType.APPLICATION_JSON)
        .expectBody()
        .jsonPath("$.[0].id").isEqualTo(2)
        .jsonPath("$.[0].description").isEqualTo("rabies shot")
        .jsonPath("$.[1].id").isEqualTo(3)
        .jsonPath("$.[1].description").isEqualTo("neutered");
  }

  @Test
  @WithMockUser(roles="OWNER_ADMIN")
  void testGetAllVisitsNotFound() throws Exception {
    visits.clear();
    given(this.clinicService.findAllVisits()).willReturn(Flux.fromIterable(visits));
    webTestClient.get().uri("/api/visits/")
        .accept(MediaType.APPLICATION_JSON)
        .exchange()
        .expectStatus()
        .isNotFound();
  }

  @Test
  @WithMockUser(roles = "OWNER_ADMIN")
  void testCreateVisitSuccess() throws Exception {
    Visit newVisit = visits.get(0);
    newVisit.setId(999);
    ObjectMapper mapper = new ObjectMapper();
    mapper.registerModule(new JavaTimeModule());
    mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    String newVisitAsJSON = mapper.writeValueAsString(visitMapper.toVisitDto(newVisit));
    System.out.println("newVisitAsJSON " + newVisitAsJSON);
    given(this.clinicService.saveVisit(any())).willReturn(Mono.just(newVisit));
    webTestClient.post().uri("/api/visits/")
        .accept(MediaType.APPLICATION_JSON)
        .contentType(MediaType.APPLICATION_JSON)
        .body(Mono.just(newVisitAsJSON), String.class)
        .exchange()
        .expectStatus().isCreated();
  }

  @Test
  @WithMockUser(roles="OWNER_ADMIN")
  void testCreateVisitError() throws Exception {
    Visit newVisit = visits.get(0);
    newVisit.setId(null);
    newVisit.setDescription(null);
    ObjectMapper mapper = new ObjectMapper();
    mapper.registerModule(new JavaTimeModule());
    String newVisitAsJSON = mapper.writeValueAsString(visitMapper.toVisitDto(newVisit));
    webTestClient.post().uri("/api/visits/")
        .accept(MediaType.APPLICATION_JSON)
        .contentType(MediaType.APPLICATION_JSON)
        .exchange()
        .expectStatus().isBadRequest();
  }

  @Test
  @WithMockUser(roles="OWNER_ADMIN")
  void testUpdateVisitSuccess() throws Exception {
    given(this.clinicService.findVisitById(2)).willReturn(Mono.just(visits.get(0)));
    Visit newVisit = visits.get(0);
    newVisit.setDescription("rabies shot test");
    ObjectMapper mapper = new ObjectMapper();
    mapper.registerModule(new JavaTimeModule());
    mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    String newVisitAsJSON = mapper.writeValueAsString(visitMapper.toVisitDto(newVisit));
    given(this.clinicService.saveVisit(newVisit)).willReturn(Mono.just(newVisit));
    webTestClient.put().uri("/api/visits/2")
        .accept(MediaType.APPLICATION_JSON)
        .contentType(MediaType.APPLICATION_JSON)
        .body(Mono.just(newVisitAsJSON), String.class)
        .exchange()
        .expectStatus().isNoContent();

    webTestClient.get().uri("/api/visits/2")
        .accept(MediaType.APPLICATION_JSON)
        .exchange()
        .expectStatus().isOk()
        .expectHeader().contentType(MediaType.APPLICATION_JSON)
        .expectBody()
        .jsonPath("$.id").isEqualTo(2)
        .jsonPath("$.description").isEqualTo("rabies shot test");
  }

  @Test
  @WithMockUser(roles="OWNER_ADMIN")
  void testUpdateVisitError() throws Exception {
    Visit newVisit = visits.get(0);
    newVisit.setDescription(null);
    ObjectMapper mapper = new ObjectMapper();
    mapper.registerModule(new JavaTimeModule());
    String newVisitAsJSON = mapper.writeValueAsString(visitMapper.toVisitDto(newVisit));
    webTestClient.put().uri("/api/visits/2")
        .accept(MediaType.APPLICATION_JSON)
        .contentType(MediaType.APPLICATION_JSON)
        .body(Mono.just(newVisitAsJSON), String.class)
        .exchange()
        .expectStatus().isBadRequest();
  }

  @Test
  @WithMockUser(roles="OWNER_ADMIN")
  void testDeleteVisitSuccess() throws Exception {
    Visit newVisit = visits.get(0);
    ObjectMapper mapper = new ObjectMapper();
    mapper.registerModule(new JavaTimeModule());
    String newVisitAsJSON = mapper.writeValueAsString(visitMapper.toVisitDto(newVisit));
    given(this.clinicService.findVisitById(2)).willReturn(Mono.just(visits.get(0)));
    given(this.clinicService.deleteVisit(any())).willReturn(Mono.empty());
    webTestClient.delete().uri("/api/visits/2")
        .accept(MediaType.APPLICATION_JSON)
        .exchange()
        .expectStatus().isNoContent();
  }

  @Test
  @WithMockUser(roles="OWNER_ADMIN")
  void testDeleteVisitError() throws Exception {
    Visit newVisit = visits.get(0);
    ObjectMapper mapper = new ObjectMapper();
    mapper.registerModule(new JavaTimeModule());
    String newVisitAsJSON = mapper.writeValueAsString(visitMapper.toVisitDto(newVisit));
    given(this.clinicService.findVisitById(999)).willReturn(Mono.empty());
    webTestClient.delete().uri("/api/visits/999")
        .accept(MediaType.APPLICATION_JSON)
        .exchange()
        .expectStatus().isNotFound();
  }
}
