package com.example.petclinic.rest.controller;

import com.example.petclinic.mapper.VisitMapper;
import com.example.petclinic.rest.api.VisitsApi;
import com.example.petclinic.rest.dto.VisitDto;
import com.example.petclinic.service.ClinicService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@CrossOrigin(exposedHeaders = "errors, content-type")
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
        return VisitsApi.super.listVisits(exchange);
    }

    @Override
    public Mono<ResponseEntity<VisitDto>> getVisit(Integer visitId,
        ServerWebExchange exchange) {
        return VisitsApi.super.getVisit(visitId, exchange);
    }

    @Override
    public Mono<ResponseEntity<VisitDto>> addVisit(Mono<VisitDto> visitDto,
        ServerWebExchange exchange) {
        return VisitsApi.super.addVisit(visitDto, exchange);
    }

    @Override
    public Mono<ResponseEntity<VisitDto>> updateVisit(Integer visitId, Mono<VisitDto> visitDto,
        ServerWebExchange exchange) {
        return VisitsApi.super.updateVisit(visitId, visitDto, exchange);
    }

    @Override
    public Mono<ResponseEntity<VisitDto>> deleteVisit(Integer visitId,
        ServerWebExchange exchange) {
        return VisitsApi.super.deleteVisit(visitId, exchange);
    }
}
