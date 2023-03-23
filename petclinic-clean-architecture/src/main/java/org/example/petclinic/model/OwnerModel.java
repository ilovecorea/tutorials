package org.example.petclinic.model;

import java.util.Set;

public interface OwnerModel {

  Integer getId();

  String getFirstName();

  String getLastName();

  String getAddress();

  String getCity();

  String getTelephone();

  Set<PetModel> getPets();

}
