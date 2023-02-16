package org.example.petclinic.persistence.impl;

import io.vertx.core.Future;
import io.vertx.sqlclient.Pool;
import io.vertx.sqlclient.templates.SqlTemplate;
import java.util.Collections;
import java.util.Optional;
import org.example.petclinic.model.PetType;
import org.example.petclinic.model.PetTypeRowMapper;
import org.example.petclinic.persistence.PetTypePersistence;

public class PetTypePersistenceImpl implements PetTypePersistence {

  private final Pool pool;

  public PetTypePersistenceImpl(Pool pool) {
    this.pool = pool;
  }

  @Override
  public Future<Optional<PetType>> findById(Integer petTypeId) {
    String sql = """
        select
          id, name
         from types
        where id = #{id}
        """.trim();
    return SqlTemplate
        .forQuery(pool, sql)
        .mapTo(PetTypeRowMapper.INSTANCE)
        .execute(Collections.singletonMap("id", petTypeId))
        .map(petType -> petType.iterator().hasNext()
            ? Optional.of(petType.iterator().next())
            : Optional.empty());
  }

  @Override
  public Future<PetType> findAll() {
    return null;
  }

  @Override
  public Future<Integer> add(PetType petType) {
    return null;
  }

  @Override
  public Future<Integer> save(PetType petType) {
    return null;
  }

  @Override
  public Future<Integer> remove(PetType petType) {
    return null;
  }
}
