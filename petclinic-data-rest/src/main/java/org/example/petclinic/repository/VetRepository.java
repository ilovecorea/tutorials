package org.example.petclinic.repository;

import org.example.petclinic.model.Vet;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(collectionResourceRel = "vets", path = "vets")
public interface VetRepository extends CrudRepository<Vet, Integer> {

}
