package com.example.petclinic.rest.controller;

import com.example.petclinic.mapper.VisitMapper;
import com.example.petclinic.model.Visit;
import com.example.petclinic.rest.api.VisitsApi;
import com.example.petclinic.rest.dto.VisitDto;
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
public class VisitRestController implements VisitsApi {

  private final ClinicService clinicService;

  private final VisitMapper visitMapper;

  public VisitRestController(ClinicService clinicService, VisitMapper visitMapper) {
    this.clinicService = clinicService;
    this.visitMapper = visitMapper;
  }

  @Override
  public Mono<ResponseEntity<Flux<VisitDto>>> listVisits(ServerWebExchange exchange) {
    Flux<Visit> visitFlux = this.clinicService.findAllVisits();
    return visitFlux
        .collectList()
        .map(visits -> visits.isEmpty()
            ? ResponseEntity.notFound().build()
            : ResponseEntity.ok(visitFlux.map(visit -> visitMapper.toVisitDto(visit))));
  }

  @Override
  public Mono<ResponseEntity<VisitDto>> getVisit(Integer visitId,
      ServerWebExchange exchange) {
    return this.clinicService.findVisitById(visitId)
        .map(visit -> ResponseEntity.ok(visitMapper.toVisitDto(visit)))
        .defaultIfEmpty(ResponseEntity.notFound().build());
  }

  @Override
  public Mono<ResponseEntity<VisitDto>> addVisit(Mono<VisitDto> visitDto,
      ServerWebExchange exchange) {
    return visitDto.flatMap(dto -> this.clinicService.saveVisit(visitMapper.toVisit(dto)))
        .map(newVisit -> ResponseEntity.created(URI.create("/api/visits/" + newVisit.getId()))
            .build());
  }

  @Override
  public Mono<ResponseEntity<VisitDto>> updateVisit(Integer visitId, Mono<VisitDto> visitDto,
      ServerWebExchange exchange) {
    return this.clinicService.findVisitById(visitId)
        .switchIfEmpty(Mono.error(new EmptyResultDataAccessException(1)))
        .flatMap(visit -> visitDto.flatMap(dto -> {
          visit.setDescription(dto.getDescription());
          visit.setDate(dto.getDate());
          visit.setPetId(dto.getPetId());
          return this.clinicService.saveVisit(visit);
        }))
        .map(v -> ResponseEntity.noContent().build());
  }

  @Override
  public Mono<ResponseEntity<VisitDto>> deleteVisit(Integer visitId,
      ServerWebExchange exchange) {
    return this.clinicService.findVisitById(visitId)
        .switchIfEmpty(Mono.error(new EmptyResultDataAccessException(1)))
        .flatMap(visit -> this.clinicService.deleteVisit(visit))
        .then(Mono.defer(() -> Mono.just(ResponseEntity.noContent().build())));
  }
}
