package com.example.petclinic.repository;

import com.example.petclinic.model.PetType;
import org.springframework.data.repository.reactive.ReactiveSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PetTypeRepository extends ReactiveSortingRepository<PetType, Integer> {
	
}
