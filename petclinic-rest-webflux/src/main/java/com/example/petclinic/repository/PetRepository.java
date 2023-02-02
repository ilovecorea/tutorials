package com.example.petclinic.repository;

import com.example.petclinic.model.Owner;
import com.example.petclinic.model.Pet;
import com.example.petclinic.model.PetType;
import java.time.LocalDate;
import org.springframework.data.repository.reactive.ReactiveSortingRepository;
import org.springframework.r2dbc.core.DatabaseClient;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public interface PetRepository extends ReactiveSortingRepository<Pet, Integer> {

  default Mono<Pet> findPetWithOwnerById(DatabaseClient client, Integer id) {
    String sql = """
        select
          p.id,
          p.name,
          p.birth_date,
          p.type_id,
          p.owner_id,
          o.id as owner_id,
          o.first_name,
          o.last_name,
          o.address,
          o.city,
          o.telephone,
          t.name as type_name,
          t.id as type_id
        from pets p
        inner join "types" t on p.type_id = t.id
        inner join owners o on p.owner_id = o.id
        where p.id = :id
        """.trim();
    return client.sql(sql)
        .bind("id", id)
        .fetch()
        .one()
        .map(row -> {
          Owner owner = Owner.builder()
              .id((Integer) row.get("owner_id"))
              .firstName((String) row.get("first_name"))
              .lastName((String) row.get("last_name"))
              .address((String) row.get("address"))
              .city((String) row.get("city"))
              .telephone((String) row.get("telephone"))
              .build();
          PetType petType = PetType.builder()
              .id((Integer) row.get("type_id"))
              .name((String) row.get("type_name"))
              .build();
          Pet pet = Pet.builder()
              .id((Integer) row.get("id"))
              .name((String) row.get("name"))
              .birthDate((LocalDate) row.get("birth_date"))
              .type(petType)
              .owner(owner)
              .build();
          return pet;
        });
  }
}
