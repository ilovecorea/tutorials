package com.example.petclinic.model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.Hibernate;
import org.springframework.beans.support.MutableSortDefinition;
import org.springframework.beans.support.PropertyComparator;

@Getter
@Setter
@Entity
@Table(name = "pets")
public class Pet extends NamedEntity {

  @Column(name = "birth_date", columnDefinition = "DATE")
  private LocalDate birthDate;

  @ManyToOne
  @JoinColumn(name = "type_id")
  private PetType type;

  @ManyToOne
  @JoinColumn(name = "owner_id")
  private Owner owner;

  @OneToMany(cascade = CascadeType.ALL, mappedBy = "pet", fetch = FetchType.EAGER)
  private Set<Visit> visits = new LinkedHashSet<>();

  @Override
  public String toString() {
    return getClass().getSimpleName() + "(" +
        "id = " + id + ", " +
        "name = " + name + ", " +
        "birthDate = " + birthDate + ", " +
        "type = " + type + ", " +
        "owner = " + owner + ")";
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) {
      return false;
    }
    Pet pet = (Pet) o;
    return id != null && Objects.equals(id, pet.id);
  }

  @Override
  public int hashCode() {
    return getClass().hashCode();
  }
}
