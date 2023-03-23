package org.example.petclinic.usecase;

import java.util.Set;
import org.example.petclinic.model.OwnerModel;

public interface OwnersUsecase {

  Set<OwnerModel> getAll();

  Set<OwnerModel> getByLastName(String lastName);

  OwnerModel get(Integer id);

  OwnerModel save(OwnerModel ownerModel);

  void delete(Integer id);

}
