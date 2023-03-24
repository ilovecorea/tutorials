package org.example.petclinic.usecases.pettype;

import org.example.petclinic.gateways.PetTypeGateway;
import org.example.petclinic.helper.Callback;
import org.springframework.stereotype.Component;

@Component
public class ListPetTypeInteractor implements ListPetTypeInputBoundary {

  private final PetTypeGateway petTypeGateway;

  public ListPetTypeInteractor(PetTypeGateway petTypeGateway) {
    this.petTypeGateway = petTypeGateway;
  }

  @Override
  public void listPetType(ListPetTypeOutputBoundary presenter) {
    petTypeGateway.findAll(
        Callback.of(
            petTypes -> presenter.success(petTypes),
            presenter::failure));
  }
}
