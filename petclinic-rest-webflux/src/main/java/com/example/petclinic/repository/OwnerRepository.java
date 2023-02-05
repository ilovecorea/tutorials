package com.example.petclinic.repository;

import com.example.petclinic.model.Owner;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

@Repository
public interface OwnerRepository extends R2dbcRepository<Owner, Integer> {

  Flux<Owner> findByLastName(String lastName);
}
