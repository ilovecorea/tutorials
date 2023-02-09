package com.example.petclinic.rest.controller;

import com.example.petclinic.mapper.SpecialtyMapper;
import com.example.petclinic.model.Specialty;
import com.example.petclinic.rest.api.SpecialtiesApi;
import com.example.petclinic.rest.dto.SpecialtyDto;
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
public class SpecialtyRestController implements SpecialtiesApi {

  private final ClinicService clinicService;

  private final SpecialtyMapper specialtyMapper;

  public SpecialtyRestController(ClinicService clinicService, SpecialtyMapper specialtyMapper) {
    this.clinicService = clinicService;
    this.specialtyMapper = specialtyMapper;
  }

  @Override
  public Mono<ResponseEntity<Flux<SpecialtyDto>>> listSpecialties(
      ServerWebExchange exchange) {
    Flux<Specialty> specialtyFlux = this.clinicService.findAllSpecialties();
    return specialtyFlux.collectList()
        .map(specialties -> specialties.isEmpty()
            ? ResponseEntity.notFound().build()
            : ResponseEntity.ok(
                specialtyFlux.map(specialty -> specialtyMapper.toSpecialtyDto(specialty))));
  }

  @Override
  public Mono<ResponseEntity<SpecialtyDto>> getSpecialty(Integer specialtyId,
      ServerWebExchange exchange) {
    return this.clinicService.findSpecialtyById(specialtyId)
        .map(specialty -> ResponseEntity.ok(specialtyMapper.toSpecialtyDto(specialty)))
        .defaultIfEmpty(ResponseEntity.notFound().build());
  }

  @Override
  public Mono<ResponseEntity<SpecialtyDto>> addSpecialty(Mono<SpecialtyDto> specialtyDto,
      ServerWebExchange exchange) {
    return specialtyDto.flatMap(dto -> {
      Specialty specialty = specialtyMapper.toSpecialty(dto);
      return this.clinicService.saveSpecialty(specialty)
          .map(newSpecilty -> ResponseEntity.created(
              URI.create("/api/specialties/" + newSpecilty.getId())).build());
    });
  }

  @Override
  public Mono<ResponseEntity<SpecialtyDto>> updateSpecialty(Integer specialtyId,
      Mono<SpecialtyDto> specialtyDto, ServerWebExchange exchange) {
    return this.clinicService.findSpecialtyById(specialtyId)
        .switchIfEmpty(Mono.error(new EmptyResultDataAccessException(1)))
        .flatMap(specialty -> specialtyDto.flatMap(dto -> {
          specialty.setName(dto.getName());
          return this.clinicService.saveSpecialty(specialty);
        }))
        .map(v -> ResponseEntity.noContent().build());
  }

  @Override
  public Mono<ResponseEntity<SpecialtyDto>> deleteSpecialty(Integer specialtyId,
      ServerWebExchange exchange) {
    return this.clinicService.findSpecialtyById(specialtyId)
        .switchIfEmpty(Mono.error(new EmptyResultDataAccessException(1)))
        .flatMap(specialty -> this.clinicService.deleteSpecialty(specialty))
        .then(Mono.defer(() -> Mono.just(ResponseEntity.noContent().build())));
  }
}
