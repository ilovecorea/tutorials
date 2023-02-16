package org.example.petclinic.persistence;

import io.vertx.core.Future;
import io.vertx.sqlclient.Pool;
import java.util.Optional;
import org.example.petclinic.model.PetType;
import org.example.petclinic.persistence.impl.PetTypePersistenceImpl;

public interface PetTypePersistence {

  static PetTypePersistenceImpl create(Pool pool) {
    return new PetTypePersistenceImpl(pool);
  }

  Future<Optional<PetType>> findById(Integer id);

  Future<PetType> findAll();

  Future<Integer> add(PetType petType);

  Future<Integer> save(PetType petType);

  Future<Integer> remove(PetType petType);

}
