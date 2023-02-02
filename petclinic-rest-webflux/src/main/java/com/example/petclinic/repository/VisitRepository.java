package com.example.petclinic.repository;

import com.example.petclinic.model.Visit;
import org.springframework.data.repository.reactive.ReactiveSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VisitRepository extends ReactiveSortingRepository<Visit, Integer> {

}
