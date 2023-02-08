package com.example.petclinic.rest.controller;

import com.example.petclinic.mapper.PetMapper;
import com.example.petclinic.rest.api.PetsApi;
import com.example.petclinic.rest.dto.PetDto;
import com.example.petclinic.service.ClinicService;
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
@RequestMapping("api")
public class PetRestController implements PetsApi {

    private final ClinicService clinicService;

    private final PetMapper petMapper;

    public PetRestController(ClinicService clinicService, PetMapper petMapper) {
        this.clinicService = clinicService;
        this.petMapper = petMapper;
    }

    @PreAuthorize("hasRole(@roles.OWNER_ADMIN)")
    @Override
    public Mono<ResponseEntity<PetDto>> getPet(Integer petId, ServerWebExchange exchange) {
        return PetsApi.super.getPet(petId, exchange);
    }

    @PreAuthorize("hasRole(@roles.OWNER_ADMIN)")
    @Override
    public Mono<ResponseEntity<Flux<PetDto>>> listPets(ServerWebExchange exchange) {
        return PetsApi.super.listPets(exchange);
    }

    @PreAuthorize("hasRole(@roles.OWNER_ADMIN)")
    @Override
    public Mono<ResponseEntity<PetDto>> updatePet(Integer petId, Mono<PetDto> petDto,
        ServerWebExchange exchange) {
        return PetsApi.super.updatePet(petId, petDto, exchange);
    }

    @PreAuthorize("hasRole(@roles.OWNER_ADMIN)")
    @Override
    public Mono<ResponseEntity<PetDto>> deletePet(Integer petId, ServerWebExchange exchange) {
        return PetsApi.super.deletePet(petId, exchange);
    }
}
