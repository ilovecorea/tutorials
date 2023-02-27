package com.example.petclinic.repository;

import com.example.petclinic.model.Owner;
import com.example.petclinic.model.Pet;
import java.beans.PropertyEditorSupport;
import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.r2dbc.core.DatabaseClient;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

@Repository
public interface OwnerRepository extends R2dbcRepository<Owner, Integer> {

//  Flux<Owner> findByLastName(String lastName);

  default Flux<Owner> findByLastName(DatabaseClient client, String lastName) {
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
          p.birth_date,
          p.type_id,
          p.owner_id
        from owners o
        inner join pets p on p.owner_id = o.id
        """.trim();
    if (StringUtils.isNotBlank(lastName)) {
      sql = sql + " where o.last_name = :lastName";
    }

    return client
        .sql(sql)
        .fetch()
        .all()
        .sort(Comparator.comparing(result -> (Integer) result.get("id")))
        .bufferUntilChanged(result -> result.get("id"))
        .map(result -> {
          List<Pet> pets = result.stream()
              .map(row -> Pet.builder()
                  .id((Integer) row.get("pet_id"))
                  .name((String) row.get("name"))
                  .birthDate((LocalDate) row.get("birth_date"))
                  .typeId((Integer) row.get("type_id"))
                  .ownerId((Integer) row.get("owner_id"))
                  .build()).toList();
          Map<String, Object> row = result.get(0);
          Owner owner = Owner.builder()
              .id((Integer) row.get("id"))
              .firstName((String) row.get("first_name"))
              .lastName((String) row.get("last_name"))
              .address((String) row.get("address"))
              .city((String) row.get("city"))
              .telephone((String) row.get("telephone"))
              .pets(pets)
              .build();

          return owner;
        });
  }
}
