package com.example.petclinic.rest.controller;

import com.example.petclinic.mapper.OwnerMapper;
import com.example.petclinic.mapper.PetMapper;
import com.example.petclinic.mapper.VisitMapper;
import com.example.petclinic.model.Owner;
import com.example.petclinic.rest.api.OwnersApi;
import com.example.petclinic.rest.dto.OwnerDto;
import com.example.petclinic.rest.dto.OwnerFieldsDto;
import com.example.petclinic.rest.dto.PetDto;
import com.example.petclinic.rest.dto.PetFieldsDto;
import com.example.petclinic.rest.dto.VisitDto;
import com.example.petclinic.rest.dto.VisitFieldsDto;
import com.example.petclinic.service.ClinicService;
import java.util.concurrent.atomic.AtomicReference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@CrossOrigin(exposedHeaders = "errors, content-type")
@RequestMapping("/api")
public class OwnerRestController implements OwnersApi {

  private final ClinicService clinicService;

  private final OwnerMapper ownerMapper;

  private final PetMapper petMapper;

  private final VisitMapper visitMapper;

  private static final Logger log = LoggerFactory.getLogger(OwnerRestController.class);

  public OwnerRestController(ClinicService clinicService, OwnerMapper ownerMapper,
      PetMapper petMapper, VisitMapper visitMapper) {
    this.clinicService = clinicService;
    this.ownerMapper = ownerMapper;
    this.petMapper = petMapper;
    this.visitMapper = visitMapper;
  }

  @Override
  @PreAuthorize("hasRole(@roles.OWNER_ADMIN)")
  public Mono<ResponseEntity<Flux<OwnerDto>>> listOwners(String lastName,
      ServerWebExchange exchange) {
    Flux<Owner> ownerDtoFlux = (lastName == null)
        ? this.clinicService.findAllOwners()
        : this.clinicService.findOwnerByLastName(lastName);
    return ownerDtoFlux
        .collectList()
        .map(list -> list.isEmpty()
            ? ResponseEntity.notFound().build()
            : ResponseEntity.ok().body(ownerDtoFlux.map(owner -> ownerMapper.toOwnerDto(owner))));
  }

  @Override
  @PreAuthorize("hasRole(@roles.OWNER_ADMIN)")
  public Mono<ResponseEntity<OwnerDto>> getOwner(Integer ownerId,
      ServerWebExchange exchange) {
    return this.clinicService.findOwnerById(ownerId)
        .map(owner -> ResponseEntity.ok().body(ownerMapper.toOwnerDto(owner)))
        .defaultIfEmpty(ResponseEntity.notFound().build());
  }

  @Override
  public Mono<ResponseEntity<OwnerDto>> addOwner(Mono<OwnerFieldsDto> ownerFieldsDto,
      ServerWebExchange exchange) {
    return ownerFieldsDto.flatMap(dto -> {
      Owner owner = ownerMapper.toOwner(dto);
      log.debug("owner:{}", owner);
      return this.clinicService.saveOwner(owner)
          .map(newOwner -> {
            HttpHeaders headers = new HttpHeaders();
            headers.setLocation(UriComponentsBuilder.newInstance()
                .path("/api/owners/{id}").buildAndExpand(newOwner.getId()).toUri());
            return new ResponseEntity<>(ownerMapper.toOwnerDto(newOwner), headers, HttpStatus.CREATED);
          })
          .onErrorResume(e -> Mono.just(ResponseEntity.badRequest().build()));
    });
  }

  @Override
  public Mono<ResponseEntity<OwnerDto>> updateOwner(Integer ownerId,
      Mono<OwnerFieldsDto> ownerFieldsDto, ServerWebExchange exchange) {
    return OwnersApi.super.updateOwner(ownerId, ownerFieldsDto, exchange);
  }

  @Override
  public Mono<ResponseEntity<OwnerDto>> deleteOwner(Integer ownerId,
      ServerWebExchange exchange) {
    return OwnersApi.super.deleteOwner(ownerId, exchange);
  }

  @Override
  public Mono<ResponseEntity<PetDto>> addPetToOwner(Integer ownerId,
      Mono<PetFieldsDto> petFieldsDto, ServerWebExchange exchange) {
    return OwnersApi.super.addPetToOwner(ownerId, petFieldsDto, exchange);
  }

  @Override
  public Mono<ResponseEntity<VisitDto>> addVisitToOwner(Integer ownerId, Integer petId,
      Mono<VisitFieldsDto> visitFieldsDto, ServerWebExchange exchange) {
    return OwnersApi.super.addVisitToOwner(ownerId, petId, visitFieldsDto, exchange);
  }
}
