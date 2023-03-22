package org.example.petclinic.repository;

import org.example.petclinic.model.Specialty;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(collectionResourceRel = "specialties", path = "specialties")
public interface SpecialtyRepository extends CrudRepository<Specialty, Integer> {

}
