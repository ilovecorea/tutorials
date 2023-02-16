package org.example.petclinic.persistence;

import io.vertx.core.Future;
import io.vertx.sqlclient.Pool;
import org.example.petclinic.model.User;
import org.example.petclinic.persistence.impl.UserPersistenceImpl;

public interface UserPersistence {

  static UserPersistenceImpl create(Pool pool) {
    return new UserPersistenceImpl(pool);
  }

  Future<Integer> add(User user);
}
