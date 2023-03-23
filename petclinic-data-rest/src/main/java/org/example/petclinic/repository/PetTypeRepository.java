package org.example.petclinic.repository;

import java.util.Set;
import org.example.petclinic.model.PetType;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(collectionResourceRel = "types", path = "types")//, excerptProjection = PetTypeProjection.class)
public interface PetTypeRepository extends PagingAndSortingRepository<PetType, Integer> {

  Set<PetType> findByName(String name);
}