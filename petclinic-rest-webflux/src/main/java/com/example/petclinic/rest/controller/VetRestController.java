package com.example.petclinic.rest.controller;

import com.example.petclinic.mapper.SpecialtyMapper;
import com.example.petclinic.mapper.VetMapper;
import com.example.petclinic.rest.api.VetsApi;
import com.example.petclinic.rest.dto.VetDto;
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
public class VetRestController implements VetsApi {

    private final ClinicService clinicService;
    private final VetMapper vetMapper;
    private final SpecialtyMapper specialtyMapper;

    public VetRestController(ClinicService clinicService, VetMapper vetMapper, SpecialtyMapper specialtyMapper) {
        this.clinicService = clinicService;
        this.vetMapper = vetMapper;
        this.specialtyMapper = specialtyMapper;
    }

    @Override
    public Mono<ResponseEntity<Flux<VetDto>>> listVets(ServerWebExchange exchange) {
        return VetsApi.super.listVets(exchange);
    }

    @Override
    public Mono<ResponseEntity<VetDto>> getVet(Integer vetId, ServerWebExchange exchange) {
        return VetsApi.super.getVet(vetId, exchange);
    }

    @Override
    public Mono<ResponseEntity<VetDto>> addVet(Mono<VetDto> vetDto,
        ServerWebExchange exchange) {
        return VetsApi.super.addVet(vetDto, exchange);
    }

    @Override
    public Mono<ResponseEntity<VetDto>> updateVet(Integer vetId, Mono<VetDto> vetDto,
        ServerWebExchange exchange) {
        return VetsApi.super.updateVet(vetId, vetDto, exchange);
    }

    @Override
    public Mono<ResponseEntity<VetDto>> deleteVet(Integer vetId, ServerWebExchange exchange) {
        return VetsApi.super.deleteVet(vetId, exchange);
    }
}
