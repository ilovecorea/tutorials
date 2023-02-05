package com.example.petclinic.repository;

import com.example.petclinic.model.Pet;
import com.example.petclinic.model.PetType;
import java.time.LocalDate;
import org.springframework.data.repository.reactive.ReactiveSortingRepository;
import org.springframework.r2dbc.core.DatabaseClient;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

@Repository
public interface PetRepository extends ReactiveSortingRepository<Pet, Integer> {

  default Flux<Pet> findWithTypeByOwnerId(DatabaseClient client, Integer ownerId) {
    String sql = """
        select
          p.id,
          p.name,
          p.birth_date,
          p.type_id,
          p.owner_id,
          t.name as type_name
         from pets p
         inner join types t on p.type_id = t.id
        where p.owner_id = :ownerId
        """.trim();
    return client
        .sql(sql)
        .bind("ownerId", ownerId)
        .fetch()
        .all()
        .map(row -> Pet.builder()
            .id((Integer) row.get("id"))
            .name((String) row.get("name"))
            .birthDate((LocalDate) row.get("birth_date"))
            .typeId((Integer) row.get("type_id"))
            .ownerId((Integer) row.get("owner_id"))
            .type(PetType.builder()
                .id((Integer) row.get("type_id"))
                .name((String) row.get("type_name"))
                .build())
            .build());
  }
}
