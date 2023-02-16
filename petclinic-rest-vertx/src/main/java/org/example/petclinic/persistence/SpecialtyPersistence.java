package org.example.petclinic.persistence;

import io.vertx.core.Future;
import io.vertx.sqlclient.Pool;
import java.util.List;
import java.util.Optional;
import org.example.petclinic.model.Specialty;
import org.example.petclinic.persistence.impl.SpecialtyPersistenceImpl;

public interface SpecialtyPersistence {

  static SpecialtyPersistenceImpl create(Pool pool) {
    return new SpecialtyPersistenceImpl(pool);
  }

  Future<Optional<Specialty>> findById(Integer id);

  Future<List<Specialty>> findAll();

  Future<Integer> add(Specialty specialty);

  Future<Integer> save(Specialty specialty);

  Future<Integer> remove(Integer id);

}
