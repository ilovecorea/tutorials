package org.example.petclinic.persistence.impl;

import io.vertx.core.Future;
import io.vertx.sqlclient.Pool;
import io.vertx.sqlclient.Row;
import io.vertx.sqlclient.templates.SqlTemplate;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
import org.example.petclinic.model.Specialty;
import org.example.petclinic.model.SpecialtyParametersMapper;
import org.example.petclinic.model.SpecialtyRowMapper;
import org.example.petclinic.persistence.SpecialtyPersistence;

public class SpecialtyPersistenceImpl implements SpecialtyPersistence {

  private final Pool pool;

  public SpecialtyPersistenceImpl(Pool pool) {
    this.pool = pool;
  }

  @Override
  public Future<Optional<Specialty>> findById(Integer id) {
    String sql = """
        select
          id, name
         from specialties
        where id = ${id}
        """.trim();
    return SqlTemplate
        .forQuery(pool, sql)
        .mapTo(SpecialtyRowMapper.INSTANCE)
        .execute(Collections.singletonMap("id", id))
        .map(specialty -> specialty.iterator().hasNext()
            ? Optional.of(specialty.iterator().next())
            : Optional.empty());
  }

  @Override
  public Future<List<Specialty>> findAll() {
    String sql = """
        select
          id, name
         from specialties
        """.trim();
    return SqlTemplate
        .forQuery(pool, sql)
        .mapTo(SpecialtyRowMapper.INSTANCE)
        .execute(Map.of())
        .map(specialties -> StreamSupport.stream(specialties.spliterator(), false)
            .collect(Collectors.toList()));
  }

  @Override
  public Future<Integer> add(Specialty specialty) {
    String sql = "insert into specialties(name) values(#{name})";
    return SqlTemplate
        .forUpdate(pool, sql)
        .mapFrom(SpecialtyParametersMapper.INSTANCE)
        .mapTo(Row::toJson)
        .execute(specialty)
        .map(result -> result.rowCount());
  }

  @Override
  public Future<Integer> save(Specialty specialty) {
    String sql = """
        update specialties
           set name = #{name}
         where id = #{id}
        """.trim();
    return SqlTemplate
        .forUpdate(pool, sql)
        .mapFrom(SpecialtyParametersMapper.INSTANCE)
        .mapTo(Row::toJson)
        .execute(specialty)
        .map(result -> result.rowCount());
  }

  @Override
  public Future<Integer> remove(Integer id) {
    String sql = "delete from owners where id = #{id}";
    return SqlTemplate
        .forUpdate(pool, sql)
        .mapTo(Row::toJson)
        .execute(Collections.singletonMap("id", id))
        .map(result -> result.rowCount());
  }
}
