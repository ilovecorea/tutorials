package org.example.petclinic.config;

import io.vertx.core.Vertx;
import io.vertx.core.spi.VerticleFactory;
import io.vertx.pgclient.PgConnectOptions;
import io.vertx.pgclient.PgPool;
import io.vertx.sqlclient.PoolOptions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan
public class AppConfig {

  @Autowired
  private DatabaseConfig databaseConfig;

  @Bean
  public Vertx vertx(VerticleFactory verticleFactory) {
    Vertx vertx = Vertx.vertx();
    vertx.registerVerticleFactory(verticleFactory);
    return vertx;
  }

  @Bean
  public PgPool pgPool(Vertx vertx) {
    PgConnectOptions pgConnectOptions =  new PgConnectOptions()
        .setPort(databaseConfig.getPort())
        .setHost(databaseConfig.getHost())
        .setDatabase(databaseConfig.getDatabase())
        .setUser(databaseConfig.getUsername())
        .setPassword(databaseConfig.getPassword());
    PoolOptions poolOptions = new PoolOptions()
        .setMaxSize(databaseConfig.getMaxPoolSize());
    return PgPool.pool(vertx, pgConnectOptions, poolOptions);
  }
}
