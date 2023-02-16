package org.example.petclinic.persistence;

import io.vertx.core.Future;
import io.vertx.sqlclient.Pool;
import java.util.List;
import java.util.Optional;
import org.example.petclinic.model.Visit;
import org.example.petclinic.persistence.impl.VisitPersistenceImpl;

public interface VisitPersistence {

  static VisitPersistenceImpl create(Pool pool) {
    return new VisitPersistenceImpl(pool);
  }

  Future<List<Visit>> findByPetId(Integer petId);

  Future<Optional<Visit>> findById(Integer id);

  Future<List<Visit>> findAll();

  Future<Integer> add(Visit visit);

  Future<Integer> save(Visit visit);

  Future<Integer> remove(Integer id);

}
