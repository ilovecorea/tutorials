package com.example.petclinic.rest.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.example.petclinic.mapper.PetMapper;
import com.example.petclinic.mapper.PetTypeMapper;
import com.example.petclinic.mapper.PetTypeMapperImpl;
import com.example.petclinic.model.PetType;
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
@WebFluxTest(PetTypeRestController.class)
@ContextConfiguration(classes = {
    ApplicationTestConfig.class,
    PetTypeMapperImpl.class
})
@Import(value = {
    GlobalErrorAttributes.class,
    GlobalErrorExceptionHandler.class,
    PetTypeRestController.class
})
public class PetTypeControllerTests {

  @MockBean
  protected ClinicService clinicService;

  @Autowired
  private PetTypeMapper petTypeMapper;

  @Autowired
  WebTestClient webTestClient;

  private List<PetType> petTypes;

  @BeforeEach
  void initPetTypes() {
    petTypes = new ArrayList<PetType>();

    PetType petType = new PetType();
    petType.setId(1);
    petType.setName("cat");
    petTypes.add(petType);

    petType = new PetType();
    petType.setId(2);
    petType.setName("dog");
    petTypes.add(petType);

    petType = new PetType();
    petType.setId(3);
    petType.setName("lizard");
    petTypes.add(petType);

    petType = new PetType();
    petType.setId(4);
    petType.setName("snake");
    petTypes.add(petType);
  }

  @Test
  @WithMockUser(roles = "OWNER_ADMIN")
  void testGetPetTypeSuccessAsOwnerAdmin() throws Exception {
    given(this.clinicService.findPetTypeById(1))
        .willReturn(Mono.just(petTypes.get(0)));
    webTestClient.get().uri("/api/pettypes/1")
        .accept(MediaType.APPLICATION_JSON)
        .exchange()
        .expectStatus().isOk()
        .expectHeader().contentType(MediaType.APPLICATION_JSON_VALUE)
        .expectBody()
        .jsonPath("$.id").isEqualTo(1)
        .jsonPath("$.name").isEqualTo("cat");
  }

  @Test
  @WithMockUser(roles = "VET_ADMIN")
  void testGetPetTypeSuccessAsVetAdmin() throws Exception {
    given(this.clinicService.findPetTypeById(1))
        .willReturn(Mono.just(petTypes.get(0)));
    webTestClient.get().uri("/api/pettypes/1")
        .accept(MediaType.APPLICATION_JSON)
        .exchange()
        .expectStatus().isOk()
        .expectHeader().contentType(MediaType.APPLICATION_JSON_VALUE)
        .expectBody()
        .jsonPath("$.id").isEqualTo(1)
        .jsonPath("$.name").isEqualTo("cat");
  }

  @Test
  @WithMockUser(roles = "OWNER_ADMIN")
  void testGetPetTypeNotFound() throws Exception {
    given(this.clinicService.findPetTypeById(999)).willReturn(Mono.empty());
    webTestClient.get().uri("/api/pettypes/999")
        .accept(MediaType.APPLICATION_JSON)
        .exchange()
        .expectStatus().isNotFound();
  }

  @Test
  @WithMockUser(roles = "OWNER_ADMIN")
  void testGetAllPetTypesSuccessAsOwnerAdmin() throws Exception {
    petTypes.remove(0);
    petTypes.remove(1);
    given(this.clinicService.findAllPetTypes())
        .willReturn(Flux.fromIterable(petTypes));
    webTestClient.get().uri("/api/pettypes/")
        .accept(MediaType.APPLICATION_JSON)
        .exchange()
        .expectStatus().isOk()
        .expectHeader().contentType(MediaType.APPLICATION_JSON_VALUE)
        .expectBody()
        .jsonPath("$.[0].id").isEqualTo(2)
        .jsonPath("$.[0].name").isEqualTo("dog")
        .jsonPath("$.[1].id").isEqualTo(4)
        .jsonPath("$.[1].name").isEqualTo("snake");
  }

  @Test
  @WithMockUser(roles = "VET_ADMIN")
  void testGetAllPetTypesSuccessAsVetAdmin() throws Exception {
    petTypes.remove(0);
    petTypes.remove(1);
    given(this.clinicService.findAllPetTypes())
        .willReturn(Flux.fromIterable(petTypes));
    webTestClient.get().uri("/api/pettypes/")
        .accept(MediaType.APPLICATION_JSON)
        .exchange()
        .expectStatus().isOk()
        .expectHeader().contentType(MediaType.APPLICATION_JSON_VALUE)
        .expectBody()
        .jsonPath("$.[0].id").isEqualTo(2)
        .jsonPath("$.[0].name").isEqualTo("dog")
        .jsonPath("$.[1].id").isEqualTo(4)
        .jsonPath("$.[1].name").isEqualTo("snake");
  }

  @Test
  @WithMockUser(roles = "VET_ADMIN")
  void testGetAllPetTypesNotFound() throws Exception {
    petTypes.clear();
    given(this.clinicService.findAllPetTypes())
        .willReturn(Flux.fromIterable(petTypes));
    webTestClient.get().uri("/api/pettypes/")
        .accept(MediaType.APPLICATION_JSON)
        .exchange()
        .expectStatus().isNotFound();
  }

  @Test
  @WithMockUser(roles = "VET_ADMIN")
  void testCreatePetTypeSuccess() throws Exception {
    PetType newPetType = petTypes.get(0);
    newPetType.setId(999);
    ObjectMapper mapper = new ObjectMapper();
    String newPetTypeAsJSON = mapper.writeValueAsString(petTypeMapper.toPetTypeDto(newPetType));
    given(this.clinicService.savePetType(any()))
        .willReturn(Mono.just(newPetType));
    webTestClient.post().uri("/api/pettypes/")
        .accept(MediaType.APPLICATION_JSON)
        .contentType(MediaType.APPLICATION_JSON)
        .body(Mono.just(newPetTypeAsJSON), String.class)
        .exchange()
        .expectStatus().isCreated();
  }

  @Test
  @WithMockUser(roles = "VET_ADMIN")
  void testCreatePetTypeError() throws Exception {
    PetType newPetType = petTypes.get(0);
    newPetType.setId(null);
    newPetType.setName(null);
    ObjectMapper mapper = new ObjectMapper();
    String newPetTypeAsJSON = mapper.writeValueAsString(petTypeMapper.toPetTypeDto(newPetType));
    webTestClient.post().uri("/api/pettypes/")
        .accept(MediaType.APPLICATION_JSON)
        .contentType(MediaType.APPLICATION_JSON)
        .body(Mono.just(newPetTypeAsJSON), String.class)
        .exchange()
        .expectStatus().isBadRequest();
  }

  @Test
  @WithMockUser(roles="VET_ADMIN")
  void testUpdatePetTypeSuccess() throws Exception {
    given(this.clinicService.findPetTypeById(2))
        .willReturn(Mono.just(petTypes.get(1)));
    PetType newPetType = petTypes.get(1);
    newPetType.setName("dog I");
    ObjectMapper mapper = new ObjectMapper();
    String newPetTypeAsJSON = mapper.writeValueAsString(petTypeMapper.toPetTypeDto(newPetType));
    given(this.clinicService.savePetType(any())).willReturn(Mono.just(newPetType));
    webTestClient.put().uri("/api/pettypes/2")
        .accept(MediaType.APPLICATION_JSON)
        .contentType(MediaType.APPLICATION_JSON)
        .body(Mono.just(newPetTypeAsJSON), String.class)
        .exchange()
        .expectStatus().isNoContent();

    webTestClient.get().uri("/api/pettypes/2")
        .accept(MediaType.APPLICATION_JSON)
        .exchange()
        .expectStatus().isOk()
        .expectBody()
        .jsonPath("$.id").isEqualTo(2)
        .jsonPath("$.name").isEqualTo("dog I");
  }

  @Test
  @WithMockUser(roles="VET_ADMIN")
  void testUpdatePetTypeError() throws Exception {
    PetType newPetType = petTypes.get(0);
    ObjectMapper mapper = new ObjectMapper();
    String newPetTypeAsJSON = mapper.writeValueAsString(newPetType);
    given(this.clinicService.findPetTypeById(1))
        .willReturn(Mono.just(petTypes.get(0)));
    given(this.clinicService.deletePetType(newPetType))
        .willReturn(Mono.empty());
    webTestClient.delete().uri("/api/pettypes/1")
        .accept(MediaType.APPLICATION_JSON)
        .exchange()
        .expectStatus().isNoContent();
  }

  @Test
  @WithMockUser(roles="VET_ADMIN")
  void testDeletePetTypeError() throws Exception {
    PetType newPetType = petTypes.get(0);
    ObjectMapper mapper = new ObjectMapper();
    String newPetTypeAsJSON = mapper.writeValueAsString(petTypeMapper.toPetTypeDto(newPetType));
    given(this.clinicService.findPetTypeById(999)).willReturn(Mono.empty());
    webTestClient.delete().uri("/api/pettypes/999")
        .accept(MediaType.APPLICATION_JSON)
        .exchange()
        .expectStatus().isNotFound();
  }
}
