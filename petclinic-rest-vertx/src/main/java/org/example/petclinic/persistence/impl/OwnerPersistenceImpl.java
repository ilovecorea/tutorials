package org.example.petclinic.persistence.impl;

import ch.qos.logback.core.joran.conditional.PropertyWrapperForScripts;
import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;
import io.vertx.sqlclient.Pool;
import io.vertx.sqlclient.Row;
import io.vertx.sqlclient.templates.SqlTemplate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
import org.apache.commons.lang3.StringUtils;
import org.example.petclinic.model.Owner;
import org.example.petclinic.model.OwnerParametersMapper;
import org.example.petclinic.model.OwnerRowMapper;
import org.example.petclinic.model.Pet;
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
  public Future<List<Owner>> findByLastName(String lastName) {
    Map<String, Object> params = new HashMap<>();
    String sql = """
        select
          o.id,
          o.first_name,
          o.last_name,
          o.address,
          o.city,
          o.telephone,
          p.id as pet_id,
          p.name,
          to_char(p.birth_date, 'YYYY-MM-DD') as birth_date,
          p.type_id,
          p.owner_id
        from owners o
        inner join pets p on p.owner_id = o.id
        """.trim();
    if (StringUtils.isNotBlank(lastName)) {
      sql = sql + " where o.last_name = #{last_name}";
      params.put("last_name", lastName);
    }
    sql = sql + " order by o.id, p.id";
    return SqlTemplate
        .forQuery(pool, sql)
        .execute(params)
        .map(rows -> {
          List<Owner> owners = new ArrayList<>();
          int compareOwnerId = -1;
          int ownerIndex = -1;
          for (Row row : rows) {
            Pet pet = new Pet()
                .setId(row.getInteger("pet_id"))
                .setName(row.getString("name"))
                .setBirthDate(row.getString("birth_date"))
                .setTypeId(row.getInteger("type_id"))
                .setOwnerId(row.getInteger("owner_id"));
            if (compareOwnerId != row.getInteger("id")) {
              Owner owner = new Owner()
                  .setId(row.getInteger("id"))
                  .setFirstName(row.getString("first_name"))
                  .setLastName(row.getString("last_name"))
                  .setAddress(row.getString("address"))
                  .setCity(row.getString("city"))
                  .setTelephone(row.getString("telephone"))
                  .setPets(new ArrayList<>());
              owners.add(owner);
              ownerIndex++;
            }
            owners.get(ownerIndex).getPets().add(pet);
            compareOwnerId = row.getInteger("id");
          }
          return owners;
        });
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
