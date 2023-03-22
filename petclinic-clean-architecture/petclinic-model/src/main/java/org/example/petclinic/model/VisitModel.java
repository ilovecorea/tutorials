package org.example.petclinic.model;

import java.time.LocalDate;

public interface VisitModel {

  Integer getId();

  LocalDate getDate();

  String getDescription();

  PetModel getPet();

}
