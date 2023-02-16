package org.example.petclinic.rest;

import io.vertx.ext.web.Router;

public class GlobalErrorHandler {

  public static void buildHandler(Router router) {
    router.errorHandler(404, rc -> rc.response().setStatusCode(404))
        .errorHandler(400, rc -> rc.response().setStatusCode(400))
        .errorHandler(401, rc -> rc.response().setStatusCode(401))
        .errorHandler(500, rc -> rc.response().setStatusCode(500));
  }

}
