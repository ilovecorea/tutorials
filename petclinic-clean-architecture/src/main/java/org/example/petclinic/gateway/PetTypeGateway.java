package org.example.petclinic.gateway;

import java.util.List;
import org.example.petclinic.helper.Callback;
import org.example.petclinic.model.PetType;

public interface PetTypeGateway {

  void findAll(Callback<List<PetType>> callback);

}
