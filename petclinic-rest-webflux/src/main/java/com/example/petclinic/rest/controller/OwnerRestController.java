package com.example.petclinic.rest.controller;

import com.example.petclinic.mapper.OwnerMapper;
import com.example.petclinic.mapper.PetMapper;
import com.example.petclinic.mapper.VisitMapper;
import com.example.petclinic.model.Owner;
import com.example.petclinic.rest.api.OwnersApi;
import com.example.petclinic.rest.dto.OwnerDto;
import com.example.petclinic.service.ClinicService;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ServerWebExchange;
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
    Flux<OwnerDto> ownerDtoFlux;
    if (lastName != null) {
      ownerDtoFlux = this.clinicService.findOwnerByLastName(lastName)
          .map(ownerMapper::toDto);
    } else {
      ownerDtoFlux = this.clinicService.findAllOwners()
          .map(ownerMapper::toDto);
    }

    return Mono
        .just(ResponseEntity.ok(ownerDtoFlux))
        .defaultIfEmpty(ResponseEntity.notFound().build());
  }

  @Override
  @PreAuthorize("hasRole(@roles.OWNER_ADMIN)")
  public Mono<ResponseEntity<OwnerDto>> getOwner(Integer ownerId,
      ServerWebExchange exchange) {
    Mono<OwnerDto> ownerDtoMono = this.clinicService.findOwnerById(ownerId)
        .map(ownerMapper::toDto)
        .map(ownerDto -> ownerDto);

    return ownerDtoMono.map(ownerDto -> ResponseEntity.ok(ownerDto));
  }
}
