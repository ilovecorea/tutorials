package org.example.petclinic.handler;

import io.vertx.core.json.JsonArray;
import io.vertx.ext.web.openapi.RouterBuilder;
import lombok.RequiredArgsConstructor;
import org.example.petclinic.entities.PetType;
import org.example.petclinic.gateways.PetTypeGateway;
import org.example.petclinic.helper.Callback;
import org.example.petclinic.usecases.pettype.ListPetTypeInteractor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PetTypeHandler {

  private final ListPetTypeInteractor listPetTypeInteractor;

  private final PetTypeGateway petTypeGateway;

  public void build(RouterBuilder routerBuilder) {
//    routerBuilder
//        .operation("listPetTypes")
//        .handler(rc -> listPetTypeInteractor.listPetType(new ListPetTypeView(rc)));
    routerBuilder
        .operation("listPetTypes")
        .handler(rc -> petTypeGateway.findAll(
            Callback.of(
                petTypes -> {
                  JsonArray jsonArray = new JsonArray();
                  petTypes.stream().map(PetType::toJson).forEach(jsonArray::add);
                  rc.response()
                      .setStatusCode(200)
                      .setStatusMessage("OK")
                      .end(jsonArray.toBuffer());
                }, throwable -> rc.fail(throwable)
            )));
  }
}
