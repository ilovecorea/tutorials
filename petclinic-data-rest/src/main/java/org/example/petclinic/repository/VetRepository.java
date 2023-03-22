package org.example.petclinic.repository;

import org.example.petclinic.model.Vet;
import org.springframework.data.repository.CrudRepository;

public interface VetRepository extends CrudRepository<Vet, Integer> {

}
