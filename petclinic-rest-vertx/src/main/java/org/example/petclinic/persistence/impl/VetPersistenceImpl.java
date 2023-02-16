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
import org.example.petclinic.model.Vet;
import org.example.petclinic.model.VetParametersMapper;
import org.example.petclinic.model.VetRowMapper;
import org.example.petclinic.persistence.VetPersistence;

public class VetPersistenceImpl implements VetPersistence {

  private final Pool pool;

  public VetPersistenceImpl(Pool pool) {
    this.pool = pool;
  }

  @Override
  public Future<Optional<Vet>> findById(Integer id) {
    String sql = """
        select
          id, first_name, last_name
         from vets
        where id = #{id}
        """.trim();
    return SqlTemplate
        .forQuery(pool, sql)
        .mapTo(VetRowMapper.INSTANCE)
        .execute(Collections.singletonMap("id", id))
        .map(vet -> vet.iterator().hasNext()
            ? Optional.of(vet.iterator().next())
            : Optional.empty());
  }

  @Override
  public Future<List<Vet>> findAll() {
    String sql = """
        select
          id, first_name, last_name
         from vets
        """.trim();
    return SqlTemplate
        .forQuery(pool, sql)
        .mapTo(VetRowMapper.INSTANCE)
        .execute(Map.of())
        .map(vets -> StreamSupport.stream(vets.spliterator(), false)
            .collect(Collectors.toList()));
  }

  @Override
  public Future<Integer> add(Vet vet) {
    String sql = """
        insert into (first_name, last_name)
        values (#{first_name}, #{last_name})
        """.trim();
    return SqlTemplate
        .forUpdate(pool, sql)
        .mapFrom(VetParametersMapper.INSTANCE)
        .execute(vet)
        .map(result -> result.rowCount());
  }

  @Override
  public Future<Integer> save(Vet vet) {
    String sql = """
        update vets
           set first_name = #{first_name},
               last_name = #{last_name}
         where id = #{id}
        """.trim();
    return SqlTemplate
        .forUpdate(pool, sql)
        .mapFrom(VetParametersMapper.INSTANCE)
        .execute(vet)
        .map(result -> result.rowCount());
  }

  @Override
  public Future<Integer> remove(Integer id) {
    String sql = "delete from vets where id = #{id}";
    return SqlTemplate
        .forUpdate(pool, sql)
        .execute(Collections.singletonMap("id", id))
        .map(result -> result.rowCount());
  }
}
