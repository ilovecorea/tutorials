package org.example.petclinic.persistence.impl;

import io.vertx.core.Future;
import io.vertx.sqlclient.Pool;
import io.vertx.sqlclient.templates.SqlTemplate;
import org.example.petclinic.model.User;
import org.example.petclinic.model.UserParametersMapper;
import org.example.petclinic.persistence.UserPersistence;

public class UserPersistenceImpl implements UserPersistence {

  private final Pool pool;

  public UserPersistenceImpl(Pool pool) {
    this.pool = pool;
  }

  @Override
  public Future<Integer> add(User user) {
    String sql = "insert into users(username, role) values (#{username}, #{role})";
    return SqlTemplate
        .forUpdate(pool, sql)
        .mapFrom(UserParametersMapper.INSTANCE)
        .execute(user)
        .map(result -> result.rowCount());
  }
}
