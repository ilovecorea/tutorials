package org.example.petclinic.persistence;

import java.util.concurrent.Future;
import liquibase.command.core.FutureRollbackCountSqlCommandStep;
import org.example.petclinic.model.PetType;

public interface PetTypePersistence {

  Future<PetType> findPetTypeById(Integer petTypeId);

  Future<PetType> findAllPetTypes();

  Future<Integer> createPetType(PetType petType);

  Future<Integer> updatePetType(PetType petType);

  Future<Integer> deletePetType(PetType petType);

}
