package org.example.petclinic;

import io.vertx.config.ConfigRetriever;
import io.vertx.config.ConfigRetrieverOptions;
import io.vertx.config.ConfigStoreOptions;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.DeploymentOptions;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import java.sql.Connection;
import java.sql.DriverManager;
import liquibase.Contexts;
import liquibase.Liquibase;
import liquibase.database.Database;
import liquibase.database.DatabaseFactory;
import liquibase.database.jvm.JdbcConnection;
import liquibase.resource.ClassLoaderResourceAccessor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PetclinicVerticle extends AbstractVerticle {

  private static final Logger log = LoggerFactory.getLogger(PetclinicVerticle.class);

  @Override
  public void start() {
    initDatabase();
    startHttpServer();
  }

  private void startHttpServer() {
  }

  private void initDatabase() {
    JsonObject config = vertx.getOrCreateContext().config();
    try {
      JsonObject db = config.getJsonObject("db");
      Connection connection = DriverManager.getConnection(
          String.format("jdbc:postgresql://%s:%d/%s",
              db.getString("host"),
              db.getInteger("port"),
              db.getString("database")),
          db.getString("username"),
          db.getString("password"));
      JdbcConnection jdbcConnection = new JdbcConnection(connection);
      Database database = DatabaseFactory.getInstance()
          .findCorrectDatabaseImplementation(jdbcConnection);
      Liquibase liquibase = new Liquibase("liquibase/master.yml",
          new ClassLoaderResourceAccessor(), database);
      liquibase.update((Contexts) null);
      log.info("Database migrations completed");
    } catch (Exception e) {
      log.error("Faild to run database migrations", e);
    }
  }

  public static void main(String[] args) {
    Vertx vertx = Vertx.vertx();
    ConfigStoreOptions store = new ConfigStoreOptions()
        .setType("file")
        .setFormat("yaml")
        .setConfig(new JsonObject().put("path", "config.yaml"));
    ConfigRetriever retriever = ConfigRetriever.create(vertx,
        new ConfigRetrieverOptions().addStore(store));
    retriever.getConfig()
        .onComplete(ar -> {
          if (ar.succeeded()) {
            DeploymentOptions options = new DeploymentOptions().setConfig(ar.result());
            vertx.deployVerticle(new PetclinicVerticle());
          } else {
            ar.cause().printStackTrace();
            System.exit(1);
          }
        });
  }
}