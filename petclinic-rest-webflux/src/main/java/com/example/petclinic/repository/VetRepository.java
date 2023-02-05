package com.example.petclinic.repository;

import com.example.petclinic.model.Specialty;
import com.example.petclinic.model.Vet;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import org.springframework.data.repository.reactive.ReactiveSortingRepository;
import org.springframework.r2dbc.core.DatabaseClient;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

@Repository
public interface VetRepository extends ReactiveSortingRepository<Vet, Integer> {

  default Flux<Vet> findAllWithSpecialties(DatabaseClient client) {
    String sql = """
        select
          v.id,
          v.first_name,
          v.last_name,
          s.id as specialty_id,
          s.name
        from vets v
          inner join vet_specialties vs on vs.vet_id = v.id
          inner join specialties s on s.id = vs.specialty_id
        order by v.id, s.name
        """.trim();

    return client.sql(sql)
        .fetch()
        .all()
        .sort(Comparator.comparing(result -> (Integer) result.get("id")))
        .bufferUntilChanged(result -> result.get("id"))
        .map(result -> {
          List<Specialty> specialties = result.stream()
              .map(row -> Specialty.builder()
                  .id((Integer) row.get("id"))
                  .name((String) row.get("name"))
                  .build())
              .toList();
          Map<String, Object> row = result.get(0);
          Vet vet = Vet.builder()
              .id((Integer) row.get("id"))
              .firstName((String) row.get("first_name"))
              .lastName((String) row.get("last_name"))
              .specialties(specialties)
              .build();

          return vet;
        });
  }
}
