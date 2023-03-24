package org.example.petclinic.controllers;

import io.vertx.ext.web.Router;
import org.springframework.stereotype.Component;

@Component
public class ErrorController {

  public void build(Router router) {
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
  }
}
