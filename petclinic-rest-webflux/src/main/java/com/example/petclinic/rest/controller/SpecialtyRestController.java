package com.example.petclinic.rest.controller;

import com.example.petclinic.mapper.SpecialtyMapper;
import com.example.petclinic.rest.api.SpecialtiesApi;
import com.example.petclinic.rest.dto.SpecialtyDto;
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
        return SpecialtiesApi.super.listSpecialties(exchange);
    }

    @Override
    public Mono<ResponseEntity<SpecialtyDto>> getSpecialty(Integer specialtyId,
        ServerWebExchange exchange) {
        return SpecialtiesApi.super.getSpecialty(specialtyId, exchange);
    }

    @Override
    public Mono<ResponseEntity<SpecialtyDto>> addSpecialty(Mono<SpecialtyDto> specialtyDto,
        ServerWebExchange exchange) {
        return SpecialtiesApi.super.addSpecialty(specialtyDto, exchange);
    }

    @Override
    public Mono<ResponseEntity<SpecialtyDto>> updateSpecialty(Integer specialtyId,
        Mono<SpecialtyDto> specialtyDto, ServerWebExchange exchange) {
        return SpecialtiesApi.super.updateSpecialty(specialtyId, specialtyDto, exchange);
    }

    @Override
    public Mono<ResponseEntity<SpecialtyDto>> deleteSpecialty(Integer specialtyId,
        ServerWebExchange exchange) {
        return SpecialtiesApi.super.deleteSpecialty(specialtyId, exchange);
    }
}
