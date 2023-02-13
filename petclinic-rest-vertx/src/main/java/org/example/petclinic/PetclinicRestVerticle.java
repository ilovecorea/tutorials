package org.example.petclinic;

import io.vertx.core.AbstractVerticle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PetclinicRestVerticle extends AbstractVerticle {

  private static final Logger log = LoggerFactory.getLogger(PetclinicRestVerticle.class);

  @Override
  public void start() {
    if (log.isInfoEnabled()) {
      log.debug("## activeProfile:{}", System.getProperty("activeProfile", ""));
    }
    vertx.createHttpServer()
        .requestHandler(req -> req.response().end("Hello World, it works !"))
        .listen(8040);
  }
}