package com.example.petclinic.repository;

import com.example.petclinic.model.Vet;
import org.springframework.data.repository.reactive.ReactiveSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VetRepository extends ReactiveSortingRepository<Vet, Integer> {

}
