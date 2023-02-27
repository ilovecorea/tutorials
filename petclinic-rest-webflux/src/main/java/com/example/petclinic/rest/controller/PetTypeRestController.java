package com.example.petclinic.rest.controller;

import com.example.petclinic.mapper.PetTypeMapper;
import com.example.petclinic.model.PetType;
import com.example.petclinic.rest.api.PettypesApi;
import com.example.petclinic.rest.dto.PetTypeDto;
import com.example.petclinic.service.ClinicService;
import java.net.URI;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("api")
public class PetTypeRestController implements PettypesApi {

  private final ClinicService clinicService;
  private final PetTypeMapper petTypeMapper;


  public PetTypeRestController(ClinicService clinicService, PetTypeMapper petTypeMapper) {
    this.clinicService = clinicService;
    this.petTypeMapper = petTypeMapper;
  }

  @Override
  public Mono<ResponseEntity<Flux<PetTypeDto>>> listPetTypes(ServerWebExchange exchange) {
    Flux<PetType> petTypeFlux = this.clinicService.findAllPetTypes();
    return petTypeFlux.collectList()
        .map(petTypes -> petTypes.isEmpty()
            ? ResponseEntity.notFound().build()
            : ResponseEntity.ok(petTypeFlux.map(petType -> petTypeMapper.toPetTypeDto(petType))));
  }

  @Override
  public Mono<ResponseEntity<PetTypeDto>> getPetType(Integer petTypeId,
      ServerWebExchange exchange) {
    return this.clinicService.findPetTypeById(petTypeId)
        .map(petType -> ResponseEntity.ok(petTypeMapper.toPetTypeDto(petType)))
        .defaultIfEmpty(ResponseEntity.notFound().build());
  }

  @Override
  public Mono<ResponseEntity<PetTypeDto>> addPetType(Mono<PetTypeDto> petTypeDto,
      ServerWebExchange exchange) {
    return petTypeDto.flatMap(dto -> {
      PetType petType = petTypeMapper.toPetType(dto);
      return this.clinicService.savePetType(petType)
          .map(p -> ResponseEntity.created(URI.create("/api/pettypes/" + petType.getId())).build());
    });
  }

  @Override
  public Mono<ResponseEntity<PetTypeDto>> updatePetType(Integer petTypeId,
      Mono<PetTypeDto> petTypeDto, ServerWebExchange exchange) {
    return this.clinicService.findPetTypeById(petTypeId)
        .switchIfEmpty(Mono.error(new EmptyResultDataAccessException(1)))
        .flatMap(petType -> petTypeDto.flatMap(dto -> {
          petType.setName(dto.getName());
          return this.clinicService.savePetType(petType);
        }))
        .map(v -> ResponseEntity.noContent().build());
  }

  @Override
  public Mono<ResponseEntity<PetTypeDto>> deletePetType(Integer petTypeId,
      ServerWebExchange exchange) {
    return this.clinicService.findPetTypeById(petTypeId)
        .switchIfEmpty(Mono.error(new EmptyResultDataAccessException(1)))
        .flatMap(petType -> this.clinicService.deletePetType(petType))
        .then(Mono.defer(() -> Mono.just(ResponseEntity.noContent().build())));
  }
}
