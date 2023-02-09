package com.example.petclinic.rest.controller;

import com.example.petclinic.mapper.PetMapper;
import com.example.petclinic.model.Pet;
import com.example.petclinic.rest.api.PetsApi;
import com.example.petclinic.rest.dto.PetDto;
import com.example.petclinic.service.ClinicService;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("api")
public class PetRestController implements PetsApi {

  private final ClinicService clinicService;

  private final PetMapper petMapper;

  public PetRestController(ClinicService clinicService, PetMapper petMapper) {
    this.clinicService = clinicService;
    this.petMapper = petMapper;
  }

  @PreAuthorize("hasRole(@roles.OWNER_ADMIN)")
  @Override
  public Mono<ResponseEntity<PetDto>> getPet(Integer petId, ServerWebExchange exchange) {
    return this.clinicService.findPetById(petId)
        .map(pet -> ResponseEntity.ok(petMapper.toPetDto(pet)));
  }

  @PreAuthorize("hasRole(@roles.OWNER_ADMIN)")
  @Override
  public Mono<ResponseEntity<Flux<PetDto>>> listPets(ServerWebExchange exchange) {
    Flux<Pet> petFlux = this.clinicService.findAllPets();
    return petFlux
        .collectList()
        .map(pets -> pets.isEmpty()
            ? ResponseEntity.notFound().build()
            : ResponseEntity.ok(petFlux.map(pet -> petMapper.toPetDto(pet))));
  }

  @PreAuthorize("hasRole(@roles.OWNER_ADMIN)")
  @Override
  public Mono<ResponseEntity<PetDto>> updatePet(Integer petId, Mono<PetDto> petDto,
      ServerWebExchange exchange) {
    return this.clinicService.findPetById(petId)
        .switchIfEmpty(Mono.error(new EmptyResultDataAccessException(1)))
        .flatMap(pet -> petDto.flatMap(dto -> {
          pet.setBirthDate(dto.getBirthDate());
          pet.setName(dto.getName());
          pet.setTypeId(dto.getType().getId());
          pet.setVisits(null);
          return this.clinicService.savePet(pet);
        }))
        .map(v -> ResponseEntity.noContent().build());
  }

  @PreAuthorize("hasRole(@roles.OWNER_ADMIN)")
  @Override
  public Mono<ResponseEntity<PetDto>> deletePet(Integer petId, ServerWebExchange exchange) {
    return this.clinicService.findPetById(petId)
        .switchIfEmpty(Mono.error(new EmptyResultDataAccessException(1)))
        .flatMap(pet -> this.clinicService.deletePet(pet))
        .then(Mono.defer(() -> Mono.just(ResponseEntity.noContent().build())));
  }
}
