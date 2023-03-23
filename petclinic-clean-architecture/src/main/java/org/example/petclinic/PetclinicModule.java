package org.example.petclinic;

import com.google.inject.AbstractModule;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.pgclient.PgConnectOptions;
import io.vertx.pgclient.PgPool;
import io.vertx.sqlclient.PoolOptions;

public class PetclinicModule extends AbstractModule {

  private final Vertx vertx;

  private final JsonObject config;

  public PetclinicModule(Vertx vertx, JsonObject config) {
    this.vertx = vertx;
    this.config = config;
  }

  @Override
  protected void configure() {
    JsonObject db = config.getJsonObject("db");
    PgConnectOptions connectOptions = new PgConnectOptions()
        .setHost(db.getString("host"))
        .setPort(db.getInteger("port"))
        .setDatabase(db.getString("database"))
        .setUser(db.getString("username"))
        .setPassword(db.getString("password"));
    PoolOptions poolOptions = new PoolOptions()
        .setMaxSize(db.getInteger("max-pool-size", 10));
    bind(PgPool.class).toInstance(PgPool.pool(vertx, connectOptions, poolOptions));
  }
}
