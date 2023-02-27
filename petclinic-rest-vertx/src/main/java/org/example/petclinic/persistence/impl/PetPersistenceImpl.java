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
import org.example.petclinic.model.Pet;
import org.example.petclinic.model.PetParametersMapper;
import org.example.petclinic.model.PetRowMapper;
import org.example.petclinic.persistence.PetPersistence;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PetPersistenceImpl implements PetPersistence {

  private final Pool pool;

  private static final Logger log = LoggerFactory.getLogger(PetPersistenceImpl.class);

  public PetPersistenceImpl(Pool pool) {
    this.pool = pool;
  }

  @Override
  public Future<Optional<Pet>> findById(Integer id) {
    String sql = """
        select
          id, name, birth_date, type_id, owner_id
         from pets
        where id = #{id}
        """.trim();
    return SqlTemplate
        .forQuery(pool, sql)
        .mapTo(PetRowMapper.INSTANCE)
        .execute(Map.of())
        .map(pet -> pet.iterator().hasNext()
            ? Optional.of(pet.iterator().next())
            : Optional.empty());
  }

  @Override
  public Future<List<Pet>> findAll() {
    String sql = """
        select
          id, name, birth_date, type_id, owner_id
         from pets
        """.trim();
    return SqlTemplate
        .forQuery(pool, sql)
        .mapTo(PetRowMapper.INSTANCE)
        .execute(Map.of())
        .map(pets -> StreamSupport.stream(pets.spliterator(), false)
            .collect(Collectors.toList()));
  }

  @Override
  public Future<Integer> add(Pet pet) {
    log.debug("## pet:{}", pet);
    String sql = """
        insert into pets(name, birth_date, type_id, owner_id)
        values (#{name}, to_date(#{birth_date}, 'YYYY-MM-DD'), #{type_id}, #{owner_id})
        """.trim();
    return SqlTemplate
        .forUpdate(pool, sql)
        .mapFrom(PetParametersMapper.INSTANCE)
        .execute(pet)
        .map(result -> result.rowCount());
  }

  @Override
  public Future<Integer> save(Pet pet) {
    String sql = """
        update pets
           set name = #{name},
               birth_date = to_date(#{birth_date}, 'YYYY-MM-DD'),
               type_id = #{type_id},
               owner_id = #{owner_id}
         where id = #{id}
        """.trim();
    return SqlTemplate
        .forUpdate(pool, sql)
        .mapFrom(PetParametersMapper.INSTANCE)
        .execute(pet)
        .map(result -> result.rowCount());
  }

  @Override
  public Future<Integer> remove(Integer id) {
    String sql = "delete from pets where id = #{id}";
    return SqlTemplate
        .forUpdate(pool, sql)
        .execute(Collections.singletonMap("id", id))
        .map(result -> result.rowCount());
  }
}
