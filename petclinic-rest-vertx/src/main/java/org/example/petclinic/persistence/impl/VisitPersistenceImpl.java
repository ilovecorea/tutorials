package org.example.petclinic.persistence.impl;

import io.vertx.core.Future;
import io.vertx.sqlclient.Pool;
import java.util.List;
import org.example.petclinic.model.Visit;
import org.example.petclinic.persistence.VisitPersistence;

public class VisitPersistenceImpl implements VisitPersistence {

  private final Pool pool;

  public VisitPersistenceImpl(Pool pool) {
    this.pool = pool;
  }

  @Override
  public Future<List<Visit>> findByPetId(Integer petId) {
    return null;
  }

  @Override
  public Future<Visit> findById(Integer visitId) {
    return null;
  }

  @Override
  public Future<List<Visit>> findAll() {
    return null;
  }

  @Override
  public Future<Integer> add(Visit visit) {
    return null;
  }

  @Override
  public Future<Integer> save(Visit visit) {
    return null;
  }

  @Override
  public Future<Integer> remove(Visit visit) {
    return null;
  }
}
