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
import org.example.petclinic.model.VetParametersMapper;
import org.example.petclinic.model.VetRowMapper;
import org.example.petclinic.model.Visit;
import org.example.petclinic.model.VisitParametersMapper;
import org.example.petclinic.model.VisitRowMapper;
import org.example.petclinic.persistence.VisitPersistence;

public class VisitPersistenceImpl implements VisitPersistence {

  private final Pool pool;

  public VisitPersistenceImpl(Pool pool) {
    this.pool = pool;
  }

  @Override
  public Future<List<Visit>> findByPetId(Integer petId) {
    String sql = """
        select
          id, pet_id, to_char(visit_date, 'YYYY-MM-DD'), description
         from visits
        where pet_id = #{pet_id}
        """.trim();
    return SqlTemplate
        .forQuery(pool, sql)
        .mapTo(VisitRowMapper.INSTANCE)
        .execute(Collections.singletonMap("pet_id", petId))
        .map(visits -> StreamSupport.stream(visits.spliterator(), false)
            .collect(Collectors.toList()));
  }

  @Override
  public Future<Optional<Visit>> findById(Integer id) {
    String sql = """
        select
          id, pet_id, to_char(visit_date, 'YYYY-MM-DD'), description
         from visits
        where id = #{id}
        """.trim();
    return SqlTemplate
        .forQuery(pool, sql)
        .mapTo(VisitRowMapper.INSTANCE)
        .execute(Map.of())
        .map(visit -> visit.iterator().hasNext()
            ? Optional.of(visit.iterator().next())
            : Optional.empty());
  }

  @Override
  public Future<List<Visit>> findAll() {
    String sql = """
        select
          id, pet_id, to_char(visit_date, 'YYYY-MM-DD'), description
         from visits
        """.trim();
    return SqlTemplate
        .forQuery(pool, sql)
        .mapTo(VisitRowMapper.INSTANCE)
        .execute(Map.of())
        .map(visits -> StreamSupport.stream(visits.spliterator(), false)
            .collect(Collectors.toList()));
  }

  @Override
  public Future<Integer> add(Visit visit) {
    String sql = """
        insert into (pet_id, visit_date, description)
        values (#{pet_id}, to_date(#{visit_date}, 'YYYY-MM-DD'), #{description})
        """.trim();
    return SqlTemplate
        .forUpdate(pool, sql)
        .mapFrom(VisitParametersMapper.INSTANCE)
        .execute(visit)
        .map(result -> result.rowCount());
  }

  @Override
  public Future<Integer> save(Visit visit) {
    String sql = """
        update visit
           set pet_id = #{pet_id},
               visit_date = to_date(#{last_name}, 'YYYY-MM-DD'),
               description = #{description}
         where id = #{id}
        """.trim();
    return SqlTemplate
        .forUpdate(pool, sql)
        .mapFrom(VisitParametersMapper.INSTANCE)
        .execute(visit)
        .map(result -> result.rowCount());
  }

  @Override
  public Future<Integer> remove(Integer id) {
    String sql = "delete from visits where id = #{id}";
    return SqlTemplate
        .forUpdate(pool, sql)
        .execute(Collections.singletonMap("id", id))
        .map(result -> result.rowCount());
  }
}
