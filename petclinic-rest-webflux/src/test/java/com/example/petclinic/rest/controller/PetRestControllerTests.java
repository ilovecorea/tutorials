package com.example.petclinic.rest.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;

import com.example.petclinic.mapper.PetMapper;
import com.example.petclinic.mapper.PetMapperImpl;
import com.example.petclinic.mapper.VisitMapper;
import com.example.petclinic.mapper.VisitMapperImpl;
import com.example.petclinic.model.Pet;
import com.example.petclinic.rest.advice.GlobalErrorAttributes;
import com.example.petclinic.rest.advice.GlobalErrorExceptionHandler;
import com.example.petclinic.rest.dto.OwnerDto;
import com.example.petclinic.rest.dto.PetDto;
import com.example.petclinic.rest.dto.PetTypeDto;
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
@WebFluxTest(PetRestController.class)
@ContextConfiguration(classes = {
    ApplicationTestConfig.class,
})
@Import(value = {
    GlobalErrorAttributes.class,
    GlobalErrorExceptionHandler.class,
    PetMapperImpl.class,
    VisitMapperImpl.class,
})
public class PetRestControllerTests {

  @MockBean
  protected ClinicService clinicService;

  @Autowired
  private PetMapper petMapper;

  @Autowired
  private VisitMapper visitMapper;

  @Autowired
  WebTestClient webTestClient;

  private List<PetDto> pets;

  @BeforeEach
  void initPets() {
    pets = new ArrayList<>();

    OwnerDto owner = new OwnerDto();
    owner.id(1).firstName("Eduardo")
        .lastName("Rodriquez")
        .address("2693 Commerce St.")
        .city("McFarland")
        .telephone("6085558763");

    PetTypeDto petType = new PetTypeDto();
    petType.id(2)
        .name("dog");

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
  }

  @Test
  @WithMockUser(roles = "OWNER_ADMIN")
  void testGetPetSuccess() throws Exception {
    given(this.clinicService.findPetById(3))
        .willReturn(Mono.just(petMapper.toPet(pets.get(0))));
    webTestClient.get().uri("/api/pets/3")
        .accept(MediaType.APPLICATION_JSON)
        .exchange()
        .expectStatus().isOk()
        .expectHeader().contentType(MediaType.APPLICATION_JSON_VALUE)
        .expectBody()
        .jsonPath("$.id").isEqualTo(3)
        .jsonPath("$.name").isEqualTo("Rosy");
  }

  @Test
  @WithMockUser(roles = "OWNER_ADMIN")
  public void testGetPetNotFound() {
    given(this.clinicService.findPetById(999)).willReturn(Mono.empty());
    webTestClient.get().uri("/api/pets/999")
        .accept(MediaType.APPLICATION_JSON)
        .exchange()
        .expectStatus().isNotFound();
  }

  @Test
  @WithMockUser(roles = "OWNER_ADMIN")
  void testGetAllPetsSuccess() throws Exception {
    final List<Pet> pets = petMapper.toPets(this.pets);
    when(this.clinicService.findAllPets()).thenReturn(Flux.fromIterable(pets));
    webTestClient.get().uri("/api/pets")
        .accept(MediaType.APPLICATION_JSON)
        .exchange()
        .expectStatus().isOk()
        .expectHeader().contentType(MediaType.APPLICATION_JSON_VALUE)
        .expectBody()
        .jsonPath("$.[0].id").isEqualTo(3)
        .jsonPath("$.[0].name").isEqualTo("Rosy")
        .jsonPath("$.[1].id").isEqualTo(4)
        .jsonPath("$.[1].name").isEqualTo("Jewel");
  }

  @Test
  @WithMockUser(roles = "OWNER_ADMIN")
  void testUpdatePetSuccess() throws Exception {
    given(this.clinicService.findPetById(3))
        .willReturn(Mono.just(petMapper.toPet(pets.get(0))));
    PetDto newPet = pets.get(0);
    newPet.setName("Rosy I");
    ObjectMapper mapper = new ObjectMapper();
    mapper.registerModule(new JavaTimeModule());
    mapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd"));
    mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    given(this.clinicService.savePet(any()))
        .willReturn(Mono.just(petMapper.toPet(newPet)));

    String newPetAsJSON = mapper.writeValueAsString(newPet);
    System.out.println(newPetAsJSON);
    webTestClient.put().uri("/api/pets/3")
        .accept(MediaType.APPLICATION_JSON)
        .contentType(MediaType.APPLICATION_JSON)
        .body(Mono.just(newPetAsJSON), String.class)
        .exchange()
        .expectStatus().isNoContent();

    System.out.println("pet.get(0):" + pets.get(0));

    webTestClient.get().uri("/api/pets/3")
        .accept(MediaType.APPLICATION_JSON)
        .exchange()
        .expectStatus().isOk()
        .expectHeader().contentType(MediaType.APPLICATION_JSON_VALUE)
        .expectBody()
        .jsonPath("$.id").isEqualTo(3)
        .jsonPath("$.name").isEqualTo("Rosy I")
        .consumeWith(System.out::println);
  }

  @Test
  @WithMockUser(roles = "OWNER_ADMIN")
  void testUpdatePetError() throws Exception {
    PetDto newPet = pets.get(0);
    newPet.setName(null);
    ObjectMapper mapper = new ObjectMapper();
    mapper.registerModule(new JavaTimeModule());
    mapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd"));
    mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    String newPetAsJSON = mapper.writeValueAsString(newPet);

    webTestClient.put().uri("/api/pets/3")
        .accept(MediaType.APPLICATION_JSON)
        .body(Mono.just(newPetAsJSON), String.class)
        .exchange()
        .expectStatus().isBadRequest();
  }

  @Test
  @WithMockUser(roles = "OWNER_ADMIN")
  void testDeletePetSuccess() throws Exception {
    PetDto newPet = pets.get(0);
    given(this.clinicService.findPetById(3))
        .willReturn(Mono.just(petMapper.toPet(pets.get(0))));
    given(this.clinicService.deletePet(any()))
        .willReturn(Mono.empty());

    webTestClient.delete().uri("/api/pets/3")
        .accept(MediaType.APPLICATION_JSON)
        .exchange()
        .expectStatus().isNoContent();
  }

  @Test
  @WithMockUser(roles = "OWNER_ADMIN")
  void testDeletePetError() throws Exception {
    PetDto newPet = pets.get(0);
    given(this.clinicService.findPetById(999))
        .willReturn(Mono.empty());
    webTestClient.delete().uri("/api/pets/999")
        .accept(MediaType.APPLICATION_JSON)
        .exchange()
        .expectStatus().isNotFound();
  }
}
