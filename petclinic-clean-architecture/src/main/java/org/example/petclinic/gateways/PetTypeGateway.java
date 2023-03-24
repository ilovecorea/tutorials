package org.example.petclinic.gateways;

import java.util.List;
import org.example.petclinic.helper.Callback;
import org.example.petclinic.entities.PetType;

public interface PetTypeGateway {

  void findAll(Callback<List<PetType>> callback);

}
