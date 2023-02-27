package com.example.petclinic.rest.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

import com.example.petclinic.mapper.SpecialtyMapper;
import com.example.petclinic.mapper.SpecialtyMapperImpl;
import com.example.petclinic.model.Specialty;
import com.example.petclinic.rest.advice.GlobalErrorAttributes;
import com.example.petclinic.rest.advice.GlobalErrorExceptionHandler;
import com.example.petclinic.service.ClinicService;
import com.fasterxml.jackson.databind.ObjectMapper;
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
@WebFluxTest(SpecialtyRestController.class)
@ContextConfiguration(classes = {
    ApplicationTestConfig.class,
    SpecialtyMapperImpl.class
})
@Import(value = {
    GlobalErrorAttributes.class,
    GlobalErrorExceptionHandler.class,
    SpecialtyRestController.class
})
public class SpecialtyRestControllerTests {

  @MockBean
  private ClinicService clinicService;

  @Autowired
  private SpecialtyMapper specialtyMapper;

  @Autowired
  WebTestClient webTestClient;

  private List<Specialty> specialties;

  @BeforeEach
  void initSpecialtys() {
    specialties = new ArrayList<Specialty>();

    Specialty specialty = new Specialty();
    specialty.setId(1);
    specialty.setName("radiology");
    specialties.add(specialty);

    specialty = new Specialty();
    specialty.setId(2);
    specialty.setName("surgery");
    specialties.add(specialty);

    specialty = new Specialty();
    specialty.setId(3);
    specialty.setName("dentistry");
    specialties.add(specialty);

  }

  @Test
  @WithMockUser(roles = "VET_ADMIN")
  void testGetSpecialtySuccess() throws Exception {
    given(this.clinicService.findSpecialtyById(1))
        .willReturn(Mono.just(specialties.get(0)));
    webTestClient.get().uri("/api/specialties/1")
        .accept(MediaType.APPLICATION_JSON)
        .exchange()
        .expectStatus().isOk()
        .expectHeader().contentType(MediaType.APPLICATION_JSON)
        .expectBody()
        .jsonPath("$.id").isEqualTo(1)
        .jsonPath("$.name").isEqualTo("radiology");
  }

  @Test
  @WithMockUser(roles = "VET_ADMIN")
  void testGetSpecialtyNotFound() throws Exception {
    given(this.clinicService.findSpecialtyById(999)).willReturn(Mono.empty());
    webTestClient.get().uri("/api/specialties/999")
        .accept(MediaType.APPLICATION_JSON)
        .exchange().expectStatus().isNotFound();
  }

  @Test
  @WithMockUser(roles = "VET_ADMIN")
  void testGetAllSpecialtysSuccess() throws Exception {
    specialties.remove(0);
    given(this.clinicService.findAllSpecialties())
        .willReturn(Flux.fromIterable(specialties));
    webTestClient.get().uri("/api/specialties/")
        .accept(MediaType.APPLICATION_JSON)
        .exchange()
        .expectStatus().isOk()
        .expectHeader().contentType(MediaType.APPLICATION_JSON)
        .expectBody()
        .jsonPath("$.[0].id").isEqualTo(2)
        .jsonPath("$.[0].name").isEqualTo("surgery")
        .jsonPath("$.[1].id").isEqualTo(3)
        .jsonPath("$.[1].name").isEqualTo("dentistry");
  }

  @Test
  @WithMockUser(roles = "VET_ADMIN")
  void testGetAllSpecialtysNotFound() throws Exception {
    specialties.clear();
    given(this.clinicService.findAllSpecialties()).willReturn(Flux.fromIterable(specialties));
    webTestClient.get().uri("/api/specialties/")
        .accept(MediaType.APPLICATION_JSON)
        .exchange()
        .expectStatus().isNotFound();
  }

  @Test
  @WithMockUser(roles = "VET_ADMIN")
  void testCreateSpecialtySuccess() throws Exception {
    Specialty newSpecialty = specialties.get(0);
    newSpecialty.setId(999);
    ObjectMapper mapper = new ObjectMapper();
    String newSpecialtyAsJSON = mapper.writeValueAsString(
        specialtyMapper.toSpecialtyDto(newSpecialty));
    given(this.clinicService.saveSpecialty(any())).willReturn(Mono.just(newSpecialty));
    webTestClient.post().uri("/api/specialties/")
        .accept(MediaType.APPLICATION_JSON)
        .contentType(MediaType.APPLICATION_JSON)
        .body(Mono.just(newSpecialtyAsJSON), String.class)
        .exchange()
        .expectStatus().isCreated();
  }

  @Test
  @WithMockUser(roles = "VET_ADMIN")
  void testCreateSpecialtyError() throws Exception {
    Specialty newSpecialty = specialties.get(0);
    newSpecialty.setId(null);
    newSpecialty.setName(null);
    ObjectMapper mapper = new ObjectMapper();
    String newSpecialtyAsJSON = mapper.writeValueAsString(
        specialtyMapper.toSpecialtyDto(newSpecialty));
    webTestClient.post().uri("/api/specialties/")
        .accept(MediaType.APPLICATION_JSON)
        .contentType(MediaType.APPLICATION_JSON)
        .body(Mono.just(newSpecialtyAsJSON), String.class)
        .exchange()
        .expectStatus().isBadRequest();
  }

  @Test
  @WithMockUser(roles = "VET_ADMIN")
  void testUpdateSpecialtySuccess() throws Exception {
    given(this.clinicService.findSpecialtyById(2))
        .willReturn(Mono.just(specialties.get(1)));
    Specialty newSpecialty = specialties.get(1);
    newSpecialty.setName("surgery I");
    ObjectMapper mapper = new ObjectMapper();
    String newSpecialtyAsJSON = mapper.writeValueAsString(
        specialtyMapper.toSpecialtyDto(newSpecialty));
    given(this.clinicService.saveSpecialty(any())).willReturn(Mono.just(newSpecialty));
    webTestClient.put().uri("/api/specialties/2")
        .accept(MediaType.APPLICATION_JSON)
        .contentType(MediaType.APPLICATION_JSON)
        .body(Mono.just(newSpecialtyAsJSON), String.class)
        .exchange()
        .expectStatus().isNoContent();

    webTestClient.get().uri("/api/specialties/2")
        .accept(MediaType.APPLICATION_JSON)
        .exchange()
        .expectStatus().isOk()
        .expectHeader().contentType(MediaType.APPLICATION_JSON)
        .expectBody()
        .jsonPath("$.id").isEqualTo(2)
        .jsonPath("$.name").isEqualTo("surgery I");
  }

  @Test
  @WithMockUser(roles = "VET_ADMIN")
  void testUpdateSpecialtyError() throws Exception {
    Specialty newSpecialty = specialties.get(0);
    newSpecialty.setName("");
    ObjectMapper mapper = new ObjectMapper();
    String newSpecialtyAsJSON = mapper.writeValueAsString(
        specialtyMapper.toSpecialtyDto(newSpecialty));
    webTestClient.put().uri("/api/specialties/1")
        .accept(MediaType.APPLICATION_JSON)
        .contentType(MediaType.APPLICATION_JSON)
        .body(Mono.just(newSpecialtyAsJSON), String.class)
        .exchange()
        .expectStatus().isBadRequest();
  }

  @Test
  @WithMockUser(roles = "VET_ADMIN")
  void testDeleteSpecialtySuccess() throws Exception {
    Specialty newSpecialty = specialties.get(0);
    ObjectMapper mapper = new ObjectMapper();
    String newSpecialtyAsJSON = mapper.writeValueAsString(
        specialtyMapper.toSpecialtyDto(newSpecialty));
    given(this.clinicService.findSpecialtyById(1)).willReturn(Mono.just(specialties.get(0)));
    given(this.clinicService.deleteSpecialty(any())).willReturn(Mono.empty());
    webTestClient.delete().uri("/api/specialties/1")
        .accept(MediaType.APPLICATION_JSON)
        .exchange().expectStatus().isNoContent();
  }

  @Test
  @WithMockUser(roles = "VET_ADMIN")
  void testDeleteSpecialtyError() throws Exception {
    Specialty newSpecialty = specialties.get(0);
    ObjectMapper mapper = new ObjectMapper();
    String newSpecialtyAsJSON = mapper.writeValueAsString(
        specialtyMapper.toSpecialtyDto(newSpecialty));
    given(this.clinicService.findSpecialtyById(999)).willReturn(Mono.empty());
    webTestClient.delete().uri("/api/specialties/999")
        .accept(MediaType.APPLICATION_JSON)
        .exchange().expectStatus().isNotFound();
  }
}
