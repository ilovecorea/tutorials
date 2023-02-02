package com.example.petclinic.model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.annotation.Transient;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Getter
@Setter
@ToString
@Table("pets")
public class Pet extends NamedEntity {

  @Column("birth_date")
  private LocalDate birthDate;

  @Transient
  private PetType type;

  @Transient
  @ToString.Exclude
  private Owner owner;

  @Transient
  private List<Visit> visits = new ArrayList<>();

  public void addVisit(Visit visit) {
    visit.setPet(this);
  }

}
