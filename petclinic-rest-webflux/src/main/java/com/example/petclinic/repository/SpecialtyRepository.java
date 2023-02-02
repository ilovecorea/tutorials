package com.example.petclinic.repository;

import com.example.petclinic.model.Specialty;
import org.springframework.data.repository.reactive.ReactiveSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SpecialtyRepository extends ReactiveSortingRepository<Specialty, Integer> {

}
