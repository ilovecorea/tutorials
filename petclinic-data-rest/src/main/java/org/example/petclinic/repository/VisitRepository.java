package org.example.petclinic.repository;

import org.example.petclinic.model.Visit;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(collectionResourceRel = "visits", path = "visits")
public interface VisitRepository extends CrudRepository<Visit, Integer> {

}
