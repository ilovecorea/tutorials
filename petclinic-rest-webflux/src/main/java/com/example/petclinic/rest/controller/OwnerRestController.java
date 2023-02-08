package com.example.petclinic.rest.controller;

import com.example.petclinic.mapper.OwnerMapper;
import com.example.petclinic.mapper.PetMapper;
import com.example.petclinic.mapper.VisitMapper;
import com.example.petclinic.model.Owner;
import com.example.petclinic.model.Pet;
import com.example.petclinic.model.Visit;
import com.example.petclinic.rest.api.OwnersApi;
import com.example.petclinic.rest.dto.OwnerDto;
import com.example.petclinic.rest.dto.OwnerFieldsDto;
import com.example.petclinic.rest.dto.PetDto;
import com.example.petclinic.rest.dto.PetFieldsDto;
import com.example.petclinic.rest.dto.VisitDto;
import com.example.petclinic.rest.dto.VisitFieldsDto;
import com.example.petclinic.service.ClinicService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.EmptyResultDataAccessException;
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
  @PreAuthorize("hasRole(@roles.OWNER_ADMIN)")
  public Mono<ResponseEntity<OwnerDto>> addOwner(Mono<OwnerFieldsDto> ownerFieldsDto,
      ServerWebExchange exchange) {
    return ownerFieldsDto.flatMap(dto -> {
      Owner owner = ownerMapper.toOwner(dto);
      log.debug("owner:{}", owner);
      return this.clinicService.saveOwner(owner)
          .map(newOwner -> {
            return ResponseEntity.created(UriComponentsBuilder.newInstance()
                .path("/api/owners/{id}").buildAndExpand(newOwner.getId()).toUri()).build();
          });
    });
  }

  @Override
  @PreAuthorize("hasRole(@roles.OWNER_ADMIN)")
  public Mono<ResponseEntity<OwnerDto>> updateOwner(Integer ownerId,
      Mono<OwnerFieldsDto> ownerFieldsDto, ServerWebExchange exchange) {
    return this.clinicService.findOwnerById(ownerId)
        .switchIfEmpty(Mono.error(new EmptyResultDataAccessException(1)))
        .flatMap(owner -> ownerFieldsDto.flatMap(dto -> {
          owner.setAddress(dto.getAddress());
          owner.setCity(dto.getCity());
          owner.setFirstName(dto.getFirstName());
          owner.setLastName(dto.getLastName());
          owner.setTelephone(dto.getTelephone());
          //owner만 저장한다.
          owner.setPets(null);
          Mono<Owner> savedOwner = this.clinicService.saveOwner(owner);
          if (log.isDebugEnabled()) {
            log.debug("## savedOwner:{}", savedOwner);
          }
          return Mono.just(ResponseEntity.noContent().build());
        }));
  }

  @Override
  @PreAuthorize("hasRole(@roles.OWNER_ADMIN)")
  public Mono<ResponseEntity<OwnerDto>> deleteOwner(Integer ownerId,
      ServerWebExchange exchange) {
    return this.clinicService.findOwnerById(ownerId)
        .switchIfEmpty(Mono.error(new EmptyResultDataAccessException(1)))
        .map(owner -> {
          this.clinicService.deleteOwner(owner);
          return ResponseEntity.noContent().build();
        });
  }

  @Override
  @PreAuthorize("hasRole(@roles.OWNER_ADMIN)")
  public Mono<ResponseEntity<PetDto>> addPetToOwner(Integer ownerId,
      Mono<PetFieldsDto> petFieldsDto, ServerWebExchange exchange) {
    return petFieldsDto.flatMap(dto -> {
      Pet pet = petMapper.toPet(dto);
      pet.setOwnerId(ownerId);
      this.clinicService.savePet(pet);
      return Mono.just(ResponseEntity.created(
          UriComponentsBuilder.newInstance().path("/api/pets/{id}")
              .buildAndExpand(pet.getId()).toUri()).build());
    });
  }

  @Override
  @PreAuthorize("hasRole(@roles.OWNER_ADMIN)")
  public Mono<ResponseEntity<VisitDto>> addVisitToOwner(Integer ownerId, Integer petId,
      Mono<VisitFieldsDto> visitFieldsDto, ServerWebExchange exchange) {
    return visitFieldsDto.flatMap(dto -> {
      Visit visit = visitMapper.toVisit(dto);
      visit.setPetId(petId);
      this.clinicService.saveVisit(visit);
      return Mono.just(ResponseEntity.created(
          UriComponentsBuilder.newInstance().path("/api/visits/{id}")
              .buildAndExpand(visit.getId()).toUri()).build());
    });
  }
}
