package org.example.petclinic.usecase;

import java.util.Set;
import org.example.petclinic.model.PetTypeModel;

public interface PetTypeUsecase {

  Set<PetTypeModel> getAll();

  PetTypeModel get(Integer id);

  PetTypeModel save(PetTypeModel petTypeModel);

  void delete(Integer id);

}
