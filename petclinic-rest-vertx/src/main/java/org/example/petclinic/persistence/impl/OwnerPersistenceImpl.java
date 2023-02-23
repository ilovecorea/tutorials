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
        where last_name = #{last_name}
        """.trim();
    return SqlTemplate
        .forQuery(pool, sql)
        .mapTo(OwnerRowMapper.INSTANCE)
        .execute(Map.of("last_name", lastName))
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
  public Future<Integer> add(Owner owner) {
    String sql = """
        insert into owners(first_name, last_name, address, city, telephone)
        values (#{first_name}, #{last_name}, #{address}, #{city}, #{telephone})
        """.trim();
    return SqlTemplate
        .forUpdate(pool, sql)
        .mapFrom(OwnerParametersMapper.INSTANCE)
        .execute(owner)
        .map(result -> result.rowCount());
  }

  @Override
  public Future<Integer> save(Owner owner) {
    String sql = """
        update owners
           set first_name = #{first_name},
               last_name = #{last_name},
               address = #{address},
               city = #{city},
               telephone = #{telephone}
         where id = #{id}
        """.trim();
    return SqlTemplate
        .forUpdate(pool, sql)
        .mapFrom(OwnerParametersMapper.INSTANCE)
        .execute(owner)
        .map(result -> result.rowCount());
  }

  @Override
  public Future<Integer> remove(Integer id) {
    String sql = "delete from owners where id = #{id}";
    return SqlTemplate
        .forUpdate(pool, sql)
        .execute(Collections.singletonMap("id", id))
        .map(result -> result.rowCount());
  }
}
