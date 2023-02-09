package com.example.petclinic.rest.controller;

import com.example.petclinic.mapper.SpecialtyMapper;
import com.example.petclinic.mapper.VetMapper;
import com.example.petclinic.model.Vet;
import com.example.petclinic.rest.api.VetsApi;
import com.example.petclinic.rest.dto.VetDto;
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
public class VetRestController implements VetsApi {

  private final ClinicService clinicService;
  private final VetMapper vetMapper;
  private final SpecialtyMapper specialtyMapper;

  public VetRestController(ClinicService clinicService, VetMapper vetMapper,
      SpecialtyMapper specialtyMapper) {
    this.clinicService = clinicService;
    this.vetMapper = vetMapper;
    this.specialtyMapper = specialtyMapper;
  }

  @Override
  public Mono<ResponseEntity<Flux<VetDto>>> listVets(ServerWebExchange exchange) {
    Flux<Vet> vetFlux = this.clinicService.findVets();
    return vetFlux
        .collectList()
        .map(vets -> vets.isEmpty()
            ? ResponseEntity.noContent().build()
            : ResponseEntity.ok(vetFlux.map(vet -> vetMapper.toVetDto(vet))));
  }

  @Override
  public Mono<ResponseEntity<VetDto>> getVet(Integer vetId, ServerWebExchange exchange) {
    return this.clinicService.findVetById(vetId)
        .map(vet -> ResponseEntity.ok(vetMapper.toVetDto(vet)))
        .defaultIfEmpty(ResponseEntity.notFound().build());
  }

  @Override
  public Mono<ResponseEntity<VetDto>> addVet(Mono<VetDto> vetDto,
      ServerWebExchange exchange) {
    return vetDto.flatMap(dto -> this.clinicService.saveVet(vetMapper.toVet(dto)))
        .map(newVet -> ResponseEntity.created(URI.create("/api/vets/" + newVet.getId()))
            .build());
  }

  @Override
  public Mono<ResponseEntity<VetDto>> updateVet(Integer vetId, Mono<VetDto> vetDto,
      ServerWebExchange exchange) {
    return this.clinicService.findVetById(vetId)
        .switchIfEmpty(Mono.error(new EmptyResultDataAccessException(1)))
        .flatMap(vet -> vetDto.flatMap(dto -> {
          vet.setFirstName(dto.getFirstName());
          vet.setLastName(dto.getLastName());
          return this.clinicService.saveVet(vet);
        }))
        .map(v -> ResponseEntity.noContent().build());
  }

  @Override
  public Mono<ResponseEntity<VetDto>> deleteVet(Integer vetId, ServerWebExchange exchange) {
    return this.clinicService.findVetById(vetId)
        .switchIfEmpty(Mono.error(new EmptyResultDataAccessException(1)))
        .flatMap(vet -> this.clinicService.deleteVet(vet))
        .then(Mono.defer(() -> Mono.just(ResponseEntity.noContent().build())));
  }
}
