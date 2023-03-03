package org.example.petclinic;

import io.vertx.config.ConfigRetriever;
import io.vertx.config.ConfigRetrieverOptions;
import io.vertx.config.ConfigStoreOptions;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.DeploymentOptions;
import io.vertx.core.Future;
import io.vertx.core.Vertx;
import io.vertx.core.eventbus.MessageConsumer;
import io.vertx.core.http.HttpServer;
import io.vertx.core.http.HttpServerOptions;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.openapi.RouterBuilder;
import io.vertx.pgclient.PgConnectOptions;
import io.vertx.pgclient.PgPool;
import io.vertx.serviceproxy.ServiceBinder;
import io.vertx.sqlclient.PoolOptions;
import java.sql.Connection;
import java.sql.DriverManager;
import liquibase.Contexts;
import liquibase.Liquibase;
import liquibase.database.Database;
import liquibase.database.DatabaseFactory;
import liquibase.database.jvm.JdbcConnection;
import liquibase.resource.ClassLoaderResourceAccessor;
import org.example.petclinic.persistence.OwnersPersistence;
import org.example.petclinic.persistence.PetPersistence;
import org.example.petclinic.persistence.PetTypePersistence;
import org.example.petclinic.persistence.SpecialtyPersistence;
import org.example.petclinic.persistence.UserPersistence;
import org.example.petclinic.persistence.VetPersistence;
import org.example.petclinic.persistence.VisitPersistence;
import org.example.petclinic.service.ClinicService;
import org.example.petclinic.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PetclinicRestVerticle extends AbstractVerticle {

  private static final Logger log = LoggerFactory.getLogger(PetclinicRestVerticle.class);
  HttpServer server;
  ServiceBinder serviceBinder;
  MessageConsumer<JsonObject> consumer;
  JsonObject config;

  /**
   * web service binding
   */
  private void bindService(PgPool pool) {
    serviceBinder = new ServiceBinder(vertx);
    //create persistence
    OwnersPersistence ownersPersistence = OwnersPersistence.create(pool);
    PetPersistence petPersistence = PetPersistence.create(pool);
    PetTypePersistence petTypePersistence = PetTypePersistence.create(pool);
    VetPersistence vetPersistence = VetPersistence.create(pool);
    VisitPersistence visitPersistence = VisitPersistence.create(pool);
    SpecialtyPersistence specialtyPersistence = SpecialtyPersistence.create(pool);
    UserPersistence userPersistence = UserPersistence.create(pool);
    //create service
    ClinicService clinicService = ClinicService.create(ownersPersistence, petPersistence,
        petTypePersistence, vetPersistence, visitPersistence, specialtyPersistence);
    UserService userService = UserService.create(userPersistence);
    //bind service
    consumer = serviceBinder
        .setAddress("petclinic.default")
        .register(ClinicService.class, clinicService);
    consumer = serviceBinder
        .setAddress("petclinic.user")
        .register(UserService.class, userService);
  }

  /**
   * create pg pool
   */
  private PgPool createPgPool() {
    JsonObject pg = config.getJsonObject("pg");
    PgConnectOptions connectOptions = new PgConnectOptions()
        .setPort(pg.getInteger("port"))
        .setHost(pg.getString("host"))
        .setDatabase(pg.getString("database"))
        .setUser(pg.getString("username"))
        .setPassword(pg.getString("password"));
    PoolOptions poolOptions = new PoolOptions()
        .setMaxSize(pg.getInteger("max-pool-size", 10));
    return PgPool.pool(vertx, connectOptions, poolOptions);
  }

  /**
   * start http server
   */
  private Future<Void> startHttpServer() {
    String contextPath = config.getString("base-path");
    return RouterBuilder.create(this.vertx, "openapi.yml")
        .onFailure(Throwable::printStackTrace)
        .compose(routerBuilder -> {
          routerBuilder.mountServicesFromExtensions();
          routerBuilder.rootHandler(rc -> {
            rc.response().headers().add("Access-Control-Allow-Origin", "*");
            String path = rc.request().path();
            //context root 경로를 치환한다.
            if (path.startsWith(contextPath)) {
              rc.reroute(path.replace(contextPath, ""));
            } else {
              rc.next();
            }
          });

          Router router = routerBuilder.createRouter();
          router.errorHandler(400, rc -> rc.response()
                  .setStatusCode(400)
                  .setStatusMessage(rc.failure().getMessage())
                  .end())
              .errorHandler(404, rc -> rc.response()
                  .setStatusCode(404)
                  .setStatusMessage("Resources not found")
                  .end())
              .errorHandler(401, rc -> rc.response()
                  .setStatusCode(401)
                  .setStatusMessage("Unauthorized")
                  .end())
              .errorHandler(500, rc -> rc.response()
                  .setStatusCode(500)
                  .setStatusMessage("Internal server error")
                  .end());

          server = vertx.createHttpServer(
                  new HttpServerOptions()
                      .setPort(9966)
                      .setHost("localhost"))
              .requestHandler(router);
          return server.listen().mapEmpty();
        });
  }

  /**
   * create table & insert data
   */
  private void createDatabase() {
    try {
      JsonObject pg = config.getJsonObject("pg");
      Connection connection = DriverManager.getConnection(
          String.format("jdbc:postgresql://%s:%d/%s",
              pg.getString("host"),
              pg.getInteger("port"),
              pg.getString("database")),
          pg.getString("username"),
          pg.getString("password"));
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

  @Override
  public void start() {
    this.config = vertx.getOrCreateContext().config();
    createDatabase();
    bindService(createPgPool());
    startHttpServer();
  }

  /**
   * Main
   *
   * @param args
   */
  public static void main(String[] args) {
    Vertx vertx = Vertx.vertx();
    String activeProfile = System.getProperty("activeProfile");
    String configPath = String.format("application-%s.yaml", activeProfile);
    ConfigStoreOptions store = new ConfigStoreOptions()
        .setType("file")
        .setFormat("yaml")
        .setConfig(new JsonObject().put("path", configPath));
    ConfigRetriever retriever = ConfigRetriever.create(vertx,
        new ConfigRetrieverOptions().addStore(store));
    retriever.getConfig()
        .onComplete(ar -> {
          if (ar.succeeded()) {
            DeploymentOptions options = new DeploymentOptions().setConfig(ar.result());
            vertx.deployVerticle(PetclinicRestVerticle.class.getName(), options);
          } else {
            ar.cause().printStackTrace();
            System.exit(1);
          }
        });

  }
}