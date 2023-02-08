package com.example.petclinic.repository;

import com.example.petclinic.model.PetType;
import java.util.Set;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

@Repository
public interface PetTypeRepository extends R2dbcRepository<PetType, Integer> {

  Flux<PetType> findByIdIn(Set<Integer> ids);

}
