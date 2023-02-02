package com.example.petclinic.repository;

import com.example.petclinic.model.Owner;
import com.example.petclinic.model.Pet;
import com.example.petclinic.model.PetType;
import java.time.LocalDate;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Map;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveSortingRepository;
import org.springframework.r2dbc.core.DatabaseClient;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public interface OwnerRepository extends ReactiveSortingRepository<Owner, Integer> {


  @Query("select * from owners where last_name = :lastName")
  Flux<Owner> findByLastName(String lastName);

  default Mono<Owner> findWithPetsById(DatabaseClient client, Integer id) {
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
          p.owner_id,
          t.name as type_name,
          t.id as type_id
        from owners o
        inner join pets p on o.id = p.owner_id
        inner join "types" t on p.type_id = t.id
        where o.id = :id
        """.trim();
    return Mono.from(
        client.sql(sql)
            .bind("id", id)
            .fetch()
            .all()
            .sort(Comparator.comparing(result -> (Integer) result.get("id")))
            .bufferUntilChanged(result -> result.get("id"))
            .map(result -> {
              List<Pet> pets = result.stream()
                  .map(row -> {
                    PetType petType = PetType.builder()
                        .id((Integer) row.get("type_id"))
                        .name((String) row.get("type_name"))
                        .build();
                    return Pet.builder()
                        .id((Integer) row.get("pet_id"))
                        .name((String) row.get("name"))
                        .birthDate((LocalDate) row.get("birth_date"))
                        .type(petType)
                        .build();
                  }).toList();

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
            }));
  }
}
