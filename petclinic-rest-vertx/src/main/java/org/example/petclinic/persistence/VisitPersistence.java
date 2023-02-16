package org.example.petclinic.persistence;

import io.vertx.core.Future;
import io.vertx.sqlclient.Pool;
import java.util.List;
import org.example.petclinic.model.Visit;
import org.example.petclinic.persistence.impl.VisitPersistenceImpl;

public interface VisitPersistence {

  static VisitPersistenceImpl create(Pool pool) {
    return new VisitPersistenceImpl(pool);
  }

  Future<List<Visit>> findByPetId(Integer petId);

  Future<Visit> findById(Integer visitId);

  Future<List<Visit>> findAll();

  Future<Integer> add(Visit visit);

  Future<Integer> save(Visit visit);

  Future<Integer> remove(Visit visit);

}
