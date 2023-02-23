package org.example.petclinic.persistence.impl;

import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;
import io.vertx.sqlclient.Pool;
import io.vertx.sqlclient.SqlClient;
import io.vertx.sqlclient.templates.SqlTemplate;
import io.vertx.sqlclient.templates.TupleMapper;
import java.util.List;
import java.util.stream.Collectors;
import org.example.petclinic.model.Role;
import org.example.petclinic.model.User;
import org.example.petclinic.model.UserParametersMapper;
import org.example.petclinic.persistence.UserPersistence;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UserPersistenceImpl implements UserPersistence {

  private final Pool pool;

  private static final Logger log = LoggerFactory.getLogger(UserPersistenceImpl.class);

  public UserPersistenceImpl(Pool pool) {
    this.pool = pool;
  }

  @Override
  public Future<Integer> add(User user) {
    log.debug("## user:{}", user);
    return pool.withTransaction(client -> addUser(client, user)
        .compose(v -> addRoles(client, user.getRoles())));
  }

  private Future<Integer> addUser(SqlClient client, User user) {
    String sql = "insert into users(username, password) values (#{username}, #{password})";
    return SqlTemplate
        .forUpdate(client, sql)
        .mapFrom(UserParametersMapper.INSTANCE)
        .execute(user)
        .map(result -> result.rowCount());
  }

  private Future<Integer> addRoles(SqlClient client, List<Role> roles) {
    String sql = "insert into roles(username, role) values (#{username}, #{role})";
    List<JsonObject> params = roles.stream()
        .map(role -> new JsonObject()
            .put("username", role.getUsername())
            .put("role", role.getName()))
        .collect(Collectors.toList());
    return SqlTemplate
        .forUpdate(client, sql)
        .mapFrom(TupleMapper.jsonObject())
        .executeBatch(params)
        .map(result -> result.rowCount());
  }
}
