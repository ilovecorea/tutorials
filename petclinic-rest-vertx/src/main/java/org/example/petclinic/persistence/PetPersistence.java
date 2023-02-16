package org.example.petclinic.persistence;

import io.vertx.core.Future;
import io.vertx.pgclient.PgPool;
import java.util.List;
import java.util.Optional;
import org.example.petclinic.model.Pet;
import org.example.petclinic.persistence.impl.PetPersistenceImpl;

public interface PetPersistence {

  static PetPersistence create(PgPool pool) {
    return new PetPersistenceImpl(pool);
  }

  Future<Optional<Pet>> findPetById(Integer id);

  Future<List<Pet>> findAllPets();

  Future<Integer> createPet(Pet pet);

  Future<Integer> updatePet(Pet pet);

  Future<Integer> deletePet(Pet pet);

}
