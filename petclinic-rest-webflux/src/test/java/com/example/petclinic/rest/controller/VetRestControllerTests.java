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

import com.example.petclinic.mapper.SpecialtyMapper;
import com.example.petclinic.mapper.SpecialtyMapperImpl;
import com.example.petclinic.mapper.VetMapper;
import com.example.petclinic.mapper.VetMapperImpl;
import com.example.petclinic.model.Vet;
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
@WebFluxTest(VetRestController.class)
@ContextConfiguration(classes = {
    ApplicationTestConfig.class
})
@Import(value = {
    GlobalErrorAttributes.class,
    GlobalErrorExceptionHandler.class,
    VetMapperImpl.class,
    SpecialtyMapperImpl.class
})
public class VetRestControllerTests {

  @MockBean
  protected ClinicService clinicService;

  @Autowired
  private VetMapper vetMapper;

  @Autowired
  private SpecialtyMapper specialtyMapper;

  @Autowired
  WebTestClient webTestClient;

  private List<Vet> vets;

  @BeforeEach
  void initVets(){
    vets = new ArrayList<Vet>();

    Vet vet = new Vet();
    vet.setId(1);
    vet.setFirstName("James");
    vet.setLastName("Carter");
    vets.add(vet);

    vet = new Vet();
    vet.setId(2);
    vet.setFirstName("Helen");
    vet.setLastName("Leary");
    vets.add(vet);

    vet = new Vet();
    vet.setId(3);
    vet.setFirstName("Linda");
    vet.setLastName("Douglas");
    vets.add(vet);
  }

  @Test
  @WithMockUser(roles="VET_ADMIN")
  void testGetVetSuccess() throws Exception {
    given(this.clinicService.findVetById(1)).willReturn(Mono.just(vets.get(0)));
    webTestClient.get().uri("/api/vets/1")
        .accept(MediaType.APPLICATION_JSON)
        .exchange()
        .expectStatus().isOk()
        .expectHeader().contentType(MediaType.APPLICATION_JSON)
        .expectBody()
        .jsonPath("$.id").isEqualTo(1)
        .jsonPath("$.firstName").isEqualTo("James");
  }

  @Test
  @WithMockUser(roles="VET_ADMIN")
  void testGetVetNotFound() throws Exception {
    given(this.clinicService.findVetById(999)).willReturn(Mono.empty());
    webTestClient.get().uri("/api/vets/999")
        .accept(MediaType.APPLICATION_JSON)
        .exchange()
        .expectStatus().isNotFound();
  }

  @Test
  @WithMockUser(roles="VET_ADMIN")
  void testGetAllVetsSuccess() throws Exception {
    given(this.clinicService.findVets()).willReturn(Flux.fromIterable(vets));
    webTestClient.get().uri("/api/vets/")
        .accept(MediaType.APPLICATION_JSON)
        .exchange()
        .expectStatus().isOk()
        .expectHeader().contentType(MediaType.APPLICATION_JSON)
        .expectBody()
        .jsonPath("$.[0].id").isEqualTo(1)
        .jsonPath("$.[0].firstName").isEqualTo("James")
        .jsonPath("$.[1].id").isEqualTo(2)
        .jsonPath("$.[1].firstName").isEqualTo("Helen");
  }

  @Test
  @WithMockUser(roles="VET_ADMIN")
  void testGetAllVetsNotFound() throws Exception {
    vets.clear();
    given(this.clinicService.findVets()).willReturn(Flux.fromIterable(vets));
    webTestClient.get().uri("/api/vets/")
        .accept(MediaType.APPLICATION_JSON)
        .exchange()
        .expectStatus().isNotFound();
  }

  @Test
  @WithMockUser(roles="VET_ADMIN")
  void testCreateVetSuccess() throws Exception {
    Vet newVet = vets.get(0);
    newVet.setId(999);
    ObjectMapper mapper = new ObjectMapper();
    String newVetAsJSON = mapper.writeValueAsString(vetMapper.toVetDto(newVet));
    given(this.clinicService.saveVet(any())).willReturn(Mono.just(newVet));
    webTestClient.post().uri("/api/vets/")
        .accept(MediaType.APPLICATION_JSON)
        .contentType(MediaType.APPLICATION_JSON)
        .body(Mono.just(newVetAsJSON), String.class)
        .exchange()
        .expectStatus().isCreated();
  }

  @Test
  @WithMockUser(roles = "VET_ADMIN")
  void testCreateVetError() throws Exception {
    Vet newVet = vets.get(0);
    newVet.setId(null);
    newVet.setFirstName(null);
    ObjectMapper mapper = new ObjectMapper();
    String newVetAsJSON = mapper.writeValueAsString(vetMapper.toVetDto(newVet));
    webTestClient.post().uri("/api/vets/")
        .accept(MediaType.APPLICATION_JSON)
        .contentType(MediaType.APPLICATION_JSON)
        .body(Mono.just(newVetAsJSON), String.class)
        .exchange()
        .expectStatus().isBadRequest();
  }

  @Test
  @WithMockUser(roles = "VET_ADMIN")
  void testUpdateVetSuccess() throws Exception {
    given(this.clinicService.findVetById(1)).willReturn(Mono.just(vets.get(0)));
    Vet newVet = vets.get(0);
    newVet.setFirstName("James");
    ObjectMapper mapper = new ObjectMapper();
    String newVetAsJSON = mapper.writeValueAsString(vetMapper.toVetDto(newVet));
    given(this.clinicService.saveVet(any())).willReturn(Mono.just(newVet));
    webTestClient.put().uri("/api/vets/1")
        .accept(MediaType.APPLICATION_JSON)
        .contentType(MediaType.APPLICATION_JSON)
        .body(Mono.just(newVetAsJSON), String.class)
        .exchange()
        .expectStatus().isNoContent();

    webTestClient.get().uri("/api/vets/1")
        .accept(MediaType.APPLICATION_JSON)
        .exchange()
        .expectStatus().isOk()
        .expectBody()
        .jsonPath("$.id").isEqualTo(1)
        .jsonPath("$.firstName").isEqualTo("James");
  }

  @Test
  @WithMockUser(roles = "VET_ADMIN")
  void testUpdateVetError() throws Exception {
    Vet newVet = vets.get(0);
    newVet.setFirstName(null);
    ObjectMapper mapper = new ObjectMapper();
    String newVetAsJSON = mapper.writeValueAsString(vetMapper.toVetDto(newVet));
    webTestClient.put().uri("/api/vets/1")
        .accept(MediaType.APPLICATION_JSON)
        .contentType(MediaType.APPLICATION_JSON)
        .exchange()
        .expectStatus()
        .isBadRequest();
  }

  @Test
  @WithMockUser(roles = "VET_ADMIN")
  void testDeleteVetSuccess() throws Exception {
    Vet newVet = vets.get(0);
    ObjectMapper mapper = new ObjectMapper();
    String newVetAsJSON = mapper.writeValueAsString(vetMapper.toVetDto(newVet));
    given(this.clinicService.findVetById(1)).willReturn(Mono.just(vets.get(0)));
    given(this.clinicService.deleteVet(any())).willReturn(Mono.empty());
    webTestClient.delete().uri("/api/vets/1")
        .accept(MediaType.APPLICATION_JSON)
        .exchange()
        .expectStatus().isNoContent();
  }

  @Test
  @WithMockUser(roles = "VET_ADMIN")
  void testDeleteVetError() throws Exception {
    Vet newVet = vets.get(0);
    ObjectMapper mapper = new ObjectMapper();
    String newVetAsJSON = mapper.writeValueAsString(vetMapper.toVetDto(newVet));
    given(this.clinicService.findVetById(999)).willReturn(Mono.empty());
    webTestClient.delete().uri("/api/vets/999")
        .accept(MediaType.APPLICATION_JSON)
        .exchange()
        .expectStatus().isNotFound();
  }
}
