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

  Future<Optional<Pet>> findById(Integer id);

  Future<List<Pet>> findAll();

  Future<Integer> add(Pet pet);

  Future<Integer> save(Pet pet);

  Future<Integer> remove(Pet pet);

}
