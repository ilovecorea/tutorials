package org.example.petclinic.model;

import java.util.Set;

public interface VetModel {

  Integer getId();

  String getFirstName();

  String getLastName();

  Set<SpecialtyModel> getSpecialties();

}
