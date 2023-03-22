package org.example.petclinic.model;

import java.util.Set;

public interface SpecialtyModel {

  Integer getId();

  String getName();

  Set<VetModel> getVets();

}
