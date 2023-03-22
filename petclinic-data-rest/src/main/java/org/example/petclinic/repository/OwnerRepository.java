package org.example.petclinic.repository;

import org.example.petclinic.model.Owner;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(collectionResourceRel = "owners", path = "owners")
public interface OwnerRepository extends CrudRepository<Owner, Integer> {

}
