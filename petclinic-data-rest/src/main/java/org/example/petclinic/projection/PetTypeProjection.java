package org.example.petclinic.projection;

import org.example.petclinic.model.PetType;
import org.springframework.data.rest.core.config.Projection;

@Projection(name = "petType", types = { PetType.class })
public interface PetTypeProjection {

  Integer getId();

  String getName();

}
