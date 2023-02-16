package org.example.petclinic.persistence.impl;

import io.vertx.core.Future;
import io.vertx.sqlclient.Pool;
import io.vertx.sqlclient.RowSet;
import io.vertx.sqlclient.templates.SqlTemplate;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
import org.example.petclinic.model.Owner;
import org.example.petclinic.model.OwnerRowMapper;
import org.example.petclinic.persistence.OwnersPersistence;

public class OwnerPersistenceImpl implements OwnersPersistence {

  private final Pool pool;

  public OwnerPersistenceImpl(Pool pool) {
    this.pool = pool;
  }

  @Override
  public Future<List<Owner>> findAll() {
    String sql = """
        select
          id, first_name, last_name, address, city, telephone
        from owners
        """;
    SqlTemplate<Map<String, Object>, RowSet<Owner>> queryTemplate = SqlTemplate
        .forQuery(pool, sql)
        .mapTo(OwnerRowMapper.INSTANCE);
    return queryTemplate
        .execute(Map.of())
        .map(owners -> StreamSupport.stream(owners.spliterator(), false)
            .collect(Collectors.toList()));
  }

  @Override
  public Future<List<Owner>> findByLastName(String lastName) {
    String sql = """
        select
          id, first_name, last_name, address, city, telephone
        from owner
        where last_name = #{lastName}
        """;
    return null;
  }

  @Override
  public Future<Optional<Owner>> findById(Integer ownerId) {
    return null;
  }

  @Override
  public Future<Owner> createOwner(Owner owner) {
    return null;
  }

  @Override
  public Future<Owner> updateOwner(Integer ownerId, Owner owner) {
    return null;
  }

  @Override
  public Future<Void> removeOwner(Integer ownerId) {
    return null;
  }
}
