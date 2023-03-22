package org.example.petclinic.projection;

import org.example.petclinic.model.Owner;
import org.example.petclinic.model.Pet;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.rest.core.config.Projection;

@Projection(name = "owner", types = { Owner.class, Pet.class})
public interface OwnerProjection {

  Integer getId();

  @Value("#{target.getFirstName() + ' ' + target.getLastName()}")
  String getName();

  String getAddress();

  String getCity();

  String getTelephone();

  @Value("#{target.getPets().size()}")
  Integer getPetCount();
}
