package org.example.petclinic.usecase;

import io.vertx.core.json.Json;
import io.vertx.core.json.JsonArray;
import io.vertx.ext.web.RoutingContext;
import java.util.List;
import org.example.petclinic.model.PetType;

public class ListPetTypeView implements ListPetTypeOutputBoundary {

  private final RoutingContext ctx;

  public ListPetTypeView(RoutingContext ctx) {
    this.ctx = ctx;
  }

  @Override
  public void success(List<PetType> petTypes) {
    JsonArray jsonArray = new JsonArray();
    petTypes.stream().map(PetType::toJson).forEach(jsonArray::add);
    ctx.response()
        .setStatusCode(200)
        .setStatusMessage("OK")
        .end(jsonArray.toBuffer());
  }

  @Override
  public void failure(Throwable throwable) {
    ctx.fail(throwable);
  }
}
