package org.example.petclinic.gateway.impl;

import io.vertx.pgclient.PgPool;
import io.vertx.sqlclient.templates.SqlTemplate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
import org.example.petclinic.gateway.PetTypeGateway;
import org.example.petclinic.helper.Callback;
import org.example.petclinic.model.PetType;
import org.example.petclinic.model.PetTypeRowMapper;
import org.springframework.stereotype.Component;

@Component
public class PetTypeGatewayImpl implements PetTypeGateway {

  private final PgPool pool;

  public PetTypeGatewayImpl(PgPool pool) {
    this.pool = pool;
  }

  @Override
  public void findAll(Callback<List<PetType>> callback) {
    String sql = """
        select 
          id, name
        from types""".trim();
    SqlTemplate
        .forQuery(pool, sql)
        .mapTo(PetTypeRowMapper.INSTANCE)
        .execute(Map.of())
        .map(pets -> StreamSupport.stream(pets.spliterator(), false)
            .collect(Collectors.toList()))
        .onSuccess(petTypes -> callback.success(petTypes))
        .onFailure(throwable -> callback.failure(throwable));
  }
}
