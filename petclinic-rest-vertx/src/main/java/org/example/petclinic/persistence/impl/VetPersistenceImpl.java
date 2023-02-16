package org.example.petclinic.persistence.impl;

import io.vertx.core.Future;
import io.vertx.sqlclient.Pool;
import java.util.List;
import org.example.petclinic.model.Vet;
import org.example.petclinic.persistence.VetPersistence;

public class VetPersistenceImpl implements VetPersistence {

  private final Pool pool;

  public VetPersistenceImpl(Pool pool) {
    this.pool = pool;
  }

  @Override
  public Future<Vet> findById(Integer id) {
    return null;
  }

  @Override
  public Future<List<Vet>> findAll() {
    return null;
  }

  @Override
  public Future<Integer> add(Vet vet) {
    return null;
  }

  @Override
  public Future<Integer> save(Vet vet) {
    return null;
  }

  @Override
  public Future<Integer> remove(Vet vet) {
    return null;
  }
}
