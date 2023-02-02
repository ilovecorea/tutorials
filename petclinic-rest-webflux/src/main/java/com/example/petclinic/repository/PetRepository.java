package com.example.petclinic.repository;

import com.example.petclinic.model.Pet;
import org.springframework.data.repository.reactive.ReactiveSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PetRepository extends ReactiveSortingRepository<Pet, Integer> {

}
