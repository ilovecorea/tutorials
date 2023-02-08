/*
 * Copyright 2016-2017 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.petclinic.rest.controller;

import com.example.petclinic.mapper.PetTypeMapper;
import com.example.petclinic.rest.api.PettypesApi;
import com.example.petclinic.rest.dto.PetTypeDto;
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
public class PetTypeRestController implements PettypesApi {

    private final ClinicService clinicService;
    private final PetTypeMapper petTypeMapper;


    public PetTypeRestController(ClinicService clinicService, PetTypeMapper petTypeMapper) {
        this.clinicService = clinicService;
        this.petTypeMapper = petTypeMapper;
    }

    @Override
    public Mono<ResponseEntity<Flux<PetTypeDto>>> listPetTypes(ServerWebExchange exchange) {
        return PettypesApi.super.listPetTypes(exchange);
    }

    @Override
    public Mono<ResponseEntity<PetTypeDto>> getPetType(Integer petTypeId,
        ServerWebExchange exchange) {
        return PettypesApi.super.getPetType(petTypeId, exchange);
    }

    @Override
    public Mono<ResponseEntity<PetTypeDto>> addPetType(Mono<PetTypeDto> petTypeDto,
        ServerWebExchange exchange) {
        return PettypesApi.super.addPetType(petTypeDto, exchange);
    }

    @Override
    public Mono<ResponseEntity<PetTypeDto>> updatePetType(Integer petTypeId,
        Mono<PetTypeDto> petTypeDto, ServerWebExchange exchange) {
        return PettypesApi.super.updatePetType(petTypeId, petTypeDto, exchange);
    }

    @Override
    public Mono<ResponseEntity<PetTypeDto>> deletePetType(Integer petTypeId,
        ServerWebExchange exchange) {
        return PettypesApi.super.deletePetType(petTypeId, exchange);
    }
}
