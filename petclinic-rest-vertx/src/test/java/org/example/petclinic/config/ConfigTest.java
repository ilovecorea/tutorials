package org.example.petclinic.config;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

import io.vertx.config.ConfigRetriever;
import io.vertx.config.ConfigRetrieverOptions;
import io.vertx.config.ConfigStoreOptions;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.junit5.VertxExtension;
import io.vertx.junit5.VertxTestContext;
import org.example.petclinic.model.PetType;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@ExtendWith(VertxExtension.class)
public class ConfigTest {

  private static final Logger log = LoggerFactory.getLogger(ConfigTest.class);

  @Test
  public void loadConfig(Vertx vertx, VertxTestContext testContext) {
    String activeProfile = System.getProperty("activeProfile");
    String configPath = String.format("application-%s.yaml", activeProfile);
    ConfigStoreOptions store = new ConfigStoreOptions()
        .setType("file")
        .setFormat("yaml")
        .setConfig(new JsonObject()
            .put("path", configPath)
        );

    ConfigRetriever retriever = ConfigRetriever.create(vertx,
        new ConfigRetrieverOptions().addStore(store));

    retriever.getConfig(testContext.succeeding(config -> testContext.verify(() -> {
      assertThat(config.getString(Config.PROFILE), is("local"));
      assertThat(config.getString(Config.BASE_PATH), is("/petclinic"));
      testContext.completeNow();
    })));
  }

  @Test
  void test() {
    JsonObject json = new JsonObject().put("id", 1).put("name", "doc");
    PetType petType = new PetType(json);
    System.out.println(petType.getName());
  }
}
