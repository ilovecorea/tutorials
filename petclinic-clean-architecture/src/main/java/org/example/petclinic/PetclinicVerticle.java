package org.example.petclinic;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.http.HttpServerOptions;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.openapi.RouterBuilder;
import lombok.extern.slf4j.Slf4j;
import org.example.petclinic.controllers.ErrorController;
import org.example.petclinic.controllers.PetTypeController;
import org.example.petclinic.usecases.ListPetTypeView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class PetclinicVerticle extends AbstractVerticle {

//  private final ListPetTypeInteractor listPetTypeInteractor;

//  public PetclinicVerticle(ListPetTypeInteractor listPetTypeInteractor) {
//    this.listPetTypeInteractor = listPetTypeInteractor;
//  }

  @Autowired
  private final ErrorController errorController;

  private final PetTypeController petTypeController;

  public PetclinicVerticle(ErrorController errorController, PetTypeController petTypeController) {
    this.errorController = errorController;
    this.petTypeController = petTypeController;
  }

  @Override
  public void start() {
    RouterBuilder.create(vertx, "openapi.yaml")
        .onFailure(Throwable::printStackTrace)
        .onSuccess(routerBuilder -> {
          routerBuilder.rootHandler(rc -> {
            rc.response().headers().add("Access-Control-Allow-Origin", "*");
            rc.next();
          });
          petTypeController.build(routerBuilder);
          Router router = routerBuilder.createRouter();
          errorController.build(router);
          vertx.createHttpServer(new HttpServerOptions()
                  .setPort(8080)
                  .setHost("localhost"))
              .requestHandler(router)
              .listen()
              .onSuccess(httpServer -> log.info("Start petclinic rest server"))
              .onFailure(throwable -> log.error("error", throwable));
        });
  }

}