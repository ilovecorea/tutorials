package org.example.petclinic.repository;

import org.example.petclinic.model.PetType;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(collectionResourceRel = "types", path = "types")
public interface PetTypeRepository extends PagingAndSortingRepository<PetType, Integer> {

}
