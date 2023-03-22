package org.example.petclinic.model;

import java.time.LocalDate;

public interface PetModel {

  Integer getId();

  String getName();

  LocalDate getBirthDate();

  PetTypeModel getType();

  OwnerModel getOwner();


}
