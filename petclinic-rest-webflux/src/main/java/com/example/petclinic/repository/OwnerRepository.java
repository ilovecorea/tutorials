package com.example.petclinic.repository;

import com.example.petclinic.model.Owner;
import org.springframework.data.repository.reactive.ReactiveSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OwnerRepository extends ReactiveSortingRepository<Owner, Integer> {

}
