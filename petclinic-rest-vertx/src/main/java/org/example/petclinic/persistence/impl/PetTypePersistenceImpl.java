package org.example.petclinic.persistence.impl;

import io.vertx.core.Future;
import io.vertx.sqlclient.Pool;
import io.vertx.sqlclient.templates.SqlTemplate;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
import org.example.petclinic.model.PetType;
import org.example.petclinic.model.PetTypeParametersMapper;
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
  public Future<List<PetType>> findAll() {
    String sql = "select id, name from types";
    return SqlTemplate
        .forQuery(pool, sql)
        .mapTo(PetTypeRowMapper.INSTANCE)
        .execute(Map.of())
        .map(petTypes -> StreamSupport.stream(petTypes.spliterator(), false)
            .collect(Collectors.toList()));
  }

  @Override
  public Future<Integer> add(PetType petType) {
    String sql = "insert into types(name) values(#{name})";
    return SqlTemplate
        .forUpdate(pool, sql)
        .mapFrom(PetTypeParametersMapper.INSTANCE)
        .execute(petType)
        .map(result -> result.rowCount());
  }

  @Override
  public Future<Integer> save(PetType petType) {
    String sql = """
        update types
          set name = #{name}
        where id = #{id}
        """.trim();
    return SqlTemplate
        .forUpdate(pool, sql)
        .mapFrom(PetTypeParametersMapper.INSTANCE)
        .execute(petType)
        .map(resutl -> resutl.rowCount());
  }

  @Override
  public Future<Integer> remove(Integer id) {
    String sql = "delete from types where id = #{id}";
    return SqlTemplate
        .forUpdate(pool, sql)
        .execute(Collections.singletonMap("id", id))
        .map(result -> result.rowCount());
  }
}
