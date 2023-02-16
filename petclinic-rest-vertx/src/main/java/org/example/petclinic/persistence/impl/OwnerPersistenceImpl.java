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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class OwnerPersistenceImpl implements OwnersPersistence {

  private final Pool pool;

  private static final Logger log = LoggerFactory.getLogger(OwnerPersistenceImpl.class);

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
    return SqlTemplate
        .forQuery(pool, sql)
        .mapTo(OwnerRowMapper.INSTANCE)
        .execute(Map.of())
        .map(owners -> StreamSupport.stream(owners.spliterator(), false)
            .collect(Collectors.toList()));
  }

  @Override
  public Future<List<Owner>> findByLastName(String lastName) {
    log.debug("## lastName:{}", lastName);
    if (StringUtils.isEmpty(lastName)) {
      return findAll();
    }
    String sql = """
        select
          id, first_name, last_name, address, city, telephone
        from owners
        where last_name = #{lastName}
        """.trim();
    return SqlTemplate
        .forQuery(pool, sql)
        .mapTo(OwnerRowMapper.INSTANCE)
        .execute(Map.of("lastName", lastName))
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
    return SqlTemplate
        .forQuery(pool, sql)
        .mapTo(OwnerRowMapper.INSTANCE)
        .execute(Collections.singletonMap("id", ownerId))
        .map(owner -> owner.iterator().hasNext()
            ? Optional.of(owner.iterator().next())
            : Optional.empty());
  }

  @Override
  public Future<Integer> createOwner(Owner owner) {
    String sql = """
        insert into owners(first_name, last_name, address, city, telephone)
        valeus (#{firstName}, #{lastName}, #{address}, #{city}, #{telephone})
        """.trim();
    return SqlTemplate
        .forUpdate(pool, sql)
        .mapFrom(OwnerParametersMapper.INSTANCE)
        .mapTo(OwnerRowMapper.INSTANCE)
        .execute(owner)
        .map(result -> result.rowCount());
  }

  @Override
  public Future<Integer> updateOwner(Owner owner) {
    String sql = """
        update owners
           set first_name = #{firstName},
               last_name = #{lastName},
               address = #{address},
               city = #{city},
               telephone = #{telephone}
         where id = #{id}
        """.trim();
    return SqlTemplate
        .forUpdate(pool, sql)
        .mapFrom(OwnerParametersMapper.INSTANCE)
        .mapTo(OwnerRowMapper.INSTANCE)
        .execute(owner)
        .map(result -> result.rowCount());
  }

  @Override
  public Future<Integer> deleteOwner(Owner owner) {
    String sql = "delete from owners where id = #{id}";
    return SqlTemplate
        .forUpdate(pool, sql)
        .mapFrom(OwnerParametersMapper.INSTANCE)
        .mapTo(Row::toJson)
        .execute(owner)
        .map(result -> result.rowCount());
  }
}
