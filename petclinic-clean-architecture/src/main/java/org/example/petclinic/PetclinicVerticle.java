package org.example.petclinic;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.http.HttpServerOptions;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.openapi.RouterBuilder;
import lombok.extern.slf4j.Slf4j;
import org.example.petclinic.delivery.ErrorDelivery;
import org.example.petclinic.usecase.ListPetTypeInteractor;
import org.example.petclinic.usecase.ListPetTypeView;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class PetclinicVerticle extends AbstractVerticle {

  private final ListPetTypeInteractor listPetTypeInteractor;

  public PetclinicVerticle(ListPetTypeInteractor listPetTypeInteractor) {
    this.listPetTypeInteractor = listPetTypeInteractor;
  }

  @Override
  public void start() {
    JsonObject config = vertx.getOrCreateContext().config();
    RouterBuilder.create(vertx, "openapi.yaml")
        .onFailure(Throwable::printStackTrace)
        .onSuccess(routerBuilder -> {
          routerBuilder.mountServicesFromExtensions();
          routerBuilder.rootHandler(rc ->
              rc.response().headers().add("Access-Control-Allow-Origin", "*"));
          routerBuilder
              .operation("listPetTypes")
              .handler(rc -> listPetTypeInteractor.listPetType(new ListPetTypeView(rc)));
          Router router = routerBuilder.createRouter();
          ErrorDelivery.build(router);
          vertx.createHttpServer(new HttpServerOptions()
                  .setPort(8080)
                  .setHost("localhost"))
              .requestHandler(router)
              .listen();
        });
  }

}