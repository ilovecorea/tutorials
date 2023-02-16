package org.example.petclinic.persistence.impl;

import io.vertx.core.Future;
import io.vertx.sqlclient.Pool;
import io.vertx.sqlclient.Row;
import io.vertx.sqlclient.templates.SqlTemplate;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
import org.example.petclinic.model.Pet;
import org.example.petclinic.model.PetParametersMapper;
import org.example.petclinic.model.PetRowMapper;
import org.example.petclinic.persistence.PetPersistence;

public class PetPersistenceImpl implements PetPersistence {

  private final Pool pool;

  public PetPersistenceImpl(Pool pool) {
    this.pool = pool;
  }

  @Override
  public Future<Optional<Pet>> findPetById(Integer id) {
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
  public Future<List<Pet>> findAllPets() {
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
  public Future<Integer> createPet(Pet pet) {
    String sql = """
        insert into pets(name, birth_date, type_id, owner_id)
        values (#{name}, to_date(#{birthDate}, 'YYYY-MM-DD'), #{typeId}, #{ownerId})
        """.trim();
    return SqlTemplate
        .forUpdate(pool, sql)
        .mapFrom(PetParametersMapper.INSTANCE)
        .mapTo(Row::toJson)
        .execute(pet)
        .map(result -> result.rowCount());
  }

  @Override
  public Future<Integer> updatePet(Pet pet) {
    String sql = """
        update pets
           set name = #{name},
               birth_date = to_date(#{birthDate}, 'YYYY-MM-DD'),
               type_id = #{typeId},
               owner_id = #{ownerId}
         where id = #{id}
        """.trim();
    return SqlTemplate
        .forUpdate(pool, sql)
        .mapFrom(PetParametersMapper.INSTANCE)
        .mapTo(Row::toJson)
        .execute(pet)
        .map(result -> result.rowCount());
  }

  @Override
  public Future<Integer> deletePet(Pet pet) {
    String sql = "delete from pets where id = #{id}";
    return SqlTemplate
        .forUpdate(pool, sql)
        .mapFrom(PetParametersMapper.INSTANCE)
        .mapTo(Row::toJson)
        .execute(pet)
        .map(result -> result.rowCount());
  }
}
