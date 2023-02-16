package org.example.petclinic.persistence;

import io.vertx.core.Future;
import io.vertx.pgclient.PgPool;
import java.util.List;
import java.util.Optional;
import org.example.petclinic.model.Owner;
import org.example.petclinic.persistence.impl.OwnerPersistenceImpl;

public interface OwnersPersistence {

  static OwnersPersistence create(PgPool pool) {
    return new OwnerPersistenceImpl(pool);
  }

  Future<List<Owner>> findAll();

  Future<List<Owner>> findByLastName(String lastName);

  Future<Optional<Owner>> findById(Integer ownerId);

  Future<Owner> createOwner(Owner owner);

  Future<Owner> updateOwner(Integer ownerId, Owner owner);

  Future<Integer> removeOwner(Integer ownerId);

}
