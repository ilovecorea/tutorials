package org.example.petclinic.controllers;

import io.vertx.ext.web.openapi.RouterBuilder;
import org.example.petclinic.usecases.pettype.ListPetTypeInteractor;
import org.example.petclinic.usecases.pettype.ListPetTypeView;
import org.springframework.stereotype.Component;

@Component
public class PetTypeController {

  private final ListPetTypeInteractor listPetTypeInteractor;

  public PetTypeController(ListPetTypeInteractor listPetTypeInteractor) {
    this.listPetTypeInteractor = listPetTypeInteractor;
  }

  public void build(RouterBuilder routerBuilder) {
    routerBuilder
        .operation("listPetTypes")
        .handler(rc -> listPetTypeInteractor.listPetType(new ListPetTypeView(rc)));
  }
}
