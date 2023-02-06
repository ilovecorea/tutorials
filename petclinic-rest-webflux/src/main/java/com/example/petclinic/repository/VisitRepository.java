package com.example.petclinic.repository;

import com.example.petclinic.model.Pet;
import com.example.petclinic.model.Visit;
import java.time.LocalDate;
import java.util.List;
import java.util.Set;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.reactive.ReactiveSortingRepository;
import org.springframework.r2dbc.core.DatabaseClient;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public interface VisitRepository extends ReactiveSortingRepository<Visit, Integer> {

  Flux<Visit> findByPetId(int petId);

  Flux<Visit> findByIdIn(Set<Integer> ids);

  default Flux<Visit> findAll(DatabaseClient client) {
    String sql = """
        select
          v.id,
          v.pet_id,
          v.visit_date,
          v.description,
          p.id as pet_id,
          p.name,
          p.birth_date,
          p.type_id,
          p.owner_id
        from visits v
          inner join pets p on p.id = v.pet_id
        """.trim();
    return client
        .sql(sql)
        .fetch()
        .all()
        .map(row -> Visit.builder()
            .id((Integer) row.get("id"))
            .petId((Integer) row.get("pet_id"))
            .date((LocalDate) row.get("visit_date"))
            .pet(Pet.builder()
                .id((Integer) row.get("pet_id"))
                .name((String) row.get("name"))
                .birthDate((LocalDate) row.get("birth_date"))
                .typeId((Integer) row.get("type_id"))
                .ownerId((Integer) row.get("owner_id"))
                .build())
            .build());
  }

  default Mono<Visit> findById(DatabaseClient client, int id) {
    String sql = """
        select
          v.id,
          v.pet_id,
          v.visit_date,
          v.description,
          p.id as pet_id,
          p.name,
          p.birth_date,
          p.type_id,
          p.owner_id
        from visits v
          inner join pets p on p.id = v.pet_id
        where v.id = :id
        """.trim();
    return client
        .sql(sql)
        .bind("id", id)
        .fetch()
        .one()
        .map(row -> Visit.builder()
            .id((Integer) row.get("id"))
            .petId((Integer) row.get("pet_id"))
            .date((LocalDate) row.get("visit_date"))
            .description((String) row.get("description"))
            .pet(Pet.builder()
                .id((Integer) row.get("pet_id"))
                .name((String) row.get("name"))
                .birthDate((LocalDate) row.get("birth_date"))
                .typeId((Integer) row.get("type_id"))
                .ownerId((Integer) row.get("owner_id"))
                .build())
            .build());
  }

  default Flux<Visit> findByPetId(DatabaseClient client, int petId) {
    String sql = """
        select
          v.id,
          v.pet_id,
          v.visit_date,
          v.description,
          p.id as pet_id,
          p.name,
          p.birth_date,
          p.type_id,
          p.owner_id
        from visits v
          inner join pets p on p.id = v.pet_id
        where v.pet_id = :petId
        """.trim();
    return client
        .sql(sql)
        .bind("petId", petId)
        .fetch()
        .all()
        .map(row -> Visit.builder()
            .id((Integer) row.get("id"))
            .petId((Integer) row.get("pet_id"))
            .date((LocalDate) row.get("visit_date"))
            .pet(Pet.builder()
                .id((Integer) row.get("pet_id"))
                .name((String) row.get("name"))
                .birthDate((LocalDate) row.get("birth_date"))
                .typeId((Integer) row.get("type_id"))
                .ownerId((Integer) row.get("owner_id"))
                .build())
            .build());
  }

  Mono<Void> deleteByPetIdIn(Set<Integer> ids);
}
