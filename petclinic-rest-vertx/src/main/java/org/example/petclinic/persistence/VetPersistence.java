package org.example.petclinic.persistence;

import io.vertx.core.Future;
import io.vertx.sqlclient.Pool;
import java.util.List;
import org.example.petclinic.model.Vet;
import org.example.petclinic.persistence.impl.VetPersistenceImpl;

public interface VetPersistence {

  static VetPersistenceImpl create(Pool pool) {
    return new VetPersistenceImpl(pool);
  }

  Future<Vet> findById(Integer id);

  Future<List<Vet>> findAll();

  Future<Integer> add(Vet vet);

  Future<Integer> save(Vet vet);

  Future<Integer> remove(Vet vet);

}
