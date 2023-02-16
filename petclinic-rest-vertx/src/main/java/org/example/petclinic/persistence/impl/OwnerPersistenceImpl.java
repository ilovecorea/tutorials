package org.example.petclinic.persistence.impl;

import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;
import io.vertx.sqlclient.Pool;
import io.vertx.sqlclient.Row;
import io.vertx.sqlclient.RowSet;
import io.vertx.sqlclient.templates.SqlTemplate;
import io.vertx.sqlclient.templates.TupleMapper;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
import org.apache.commons.lang3.StringUtils;
import org.example.petclinic.model.Owner;
import org.example.petclinic.model.OwnerParametersMapper;
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
        """.trim();
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
    if (StringUtils.isEmpty(lastName)) {
      return findAll();
    }
    String sql = """
        select
          id, first_name, last_name, address, city, telephone
        from owners
        where last_name = #{lastName}
        """.trim();
    SqlTemplate<Map<String, Object>, RowSet<Owner>> template = SqlTemplate
        .forQuery(pool, sql)
        .mapTo(OwnerRowMapper.INSTANCE);
    return template
        .execute(Map.of())
        .map(owners -> StreamSupport.stream(owners.spliterator(), false)
            .collect(Collectors.toList()));
  }

  @Override
  public Future<Optional<Owner>> findById(Integer ownerId) {
    String sql = """
        select 
          id, first_name, last_name, address, city, telephone
         from owners
        where id = #{id}
        """.trim();
    SqlTemplate<Map<String, Object>, RowSet<Owner>> template = SqlTemplate
        .forQuery(pool, sql)
        .mapTo(OwnerRowMapper.INSTANCE);
    return template
        .execute(Collections.singletonMap("id", ownerId))
        .map(owner -> owner.iterator().hasNext()
            ? Optional.of(owner.iterator().next())
            : Optional.empty());
  }

  @Override
  public Future<Owner> createOwner(Owner owner) {
    String sql = """
        insert into owners(first_name, last_name, address, city, telephone)
        valeus (#{firstName}, #{lastName}, #{address}, #{city}, #{telephone})
        """.trim();
    SqlTemplate<Owner, RowSet<Owner>> template = SqlTemplate
        .forUpdate(pool, sql)
        .mapFrom(OwnerParametersMapper.INSTANCE)
        .mapTo(OwnerRowMapper.INSTANCE);
    return template.execute(owner)
        .map(result -> result.iterator().next());
  }

  @Override
  public Future<Owner> updateOwner(Integer ownerId, Owner owner) {
    String sql = """
        update owners
           set first_name = #{firstName},
               last_name = #{lastName},
               address = #{address},
               city = #{city},
               telephone = #{telephone}
         where id = #{id}
        """.trim();
    SqlTemplate<Owner, RowSet<Owner>> template = SqlTemplate
        .forUpdate(pool, sql)
        .mapFrom(OwnerParametersMapper.INSTANCE)
        .mapTo(OwnerRowMapper.INSTANCE);
    return template.execute(owner)
        .map(result -> result.iterator().next());
  }

  @Override
  public Future<Integer> removeOwner(Integer ownerId) {
    String sql = "delete from owners where id = #{id}";
    return SqlTemplate
        .forUpdate(pool, sql)
        .mapFrom(TupleMapper.jsonObject())
        .mapTo(Row::toJson)
        .execute(JsonObject.of("id", ownerId))
        .map(result -> result.rowCount());
  }
}
