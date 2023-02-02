package com.example.petclinic.model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import org.springframework.beans.support.MutableSortDefinition;
import org.springframework.beans.support.PropertyComparator;

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
  private Set<Visit> visits;

  public LocalDate getBirthDate() {
    return this.birthDate;
  }

  public void setBirthDate(LocalDate birthDate) {
    this.birthDate = birthDate;
  }

  public PetType getType() {
    return this.type;
  }

  public void setType(PetType type) {
    this.type = type;
  }

  public Owner getOwner() {
    return this.owner;
  }

  public void setOwner(Owner owner) {
    this.owner = owner;
  }

  protected Set<Visit> getVisitsInternal() {
    if (this.visits == null) {
      this.visits = new HashSet<>();
    }
    return this.visits;
  }

  protected void setVisitsInternal(Set<Visit> visits) {
    this.visits = visits;
  }

  public List<Visit> getVisits() {
    List<Visit> sortedVisits = new ArrayList<>(getVisitsInternal());
    PropertyComparator.sort(sortedVisits, new MutableSortDefinition("date", false, false));
    return Collections.unmodifiableList(sortedVisits);
  }

  public void setVisits(List<Visit> visits) {
    this.visits = new HashSet<>(visits);
  }

  public void addVisit(Visit visit) {
    getVisitsInternal().add(visit);
    visit.setPet(this);
  }

}
