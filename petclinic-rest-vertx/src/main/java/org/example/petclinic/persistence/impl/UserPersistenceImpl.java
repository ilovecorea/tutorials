package org.example.petclinic.persistence.impl;

import io.vertx.sqlclient.Pool;
import java.util.concurrent.Future;
import org.example.petclinic.model.User;
import org.example.petclinic.persistence.UserPersistence;

public class UserPersistenceImpl implements UserPersistence {

  private final Pool pool;

  public UserPersistenceImpl(Pool pool) {
    this.pool = pool;
  }

  @Override
  public Future<Integer> add(User user) {
    return null;
  }
}
