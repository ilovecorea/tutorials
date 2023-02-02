package com.example.petclinic.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.Digits;
import javax.validation.constraints.NotEmpty;
import org.springframework.beans.support.MutableSortDefinition;
import org.springframework.beans.support.PropertyComparator;
import org.springframework.core.style.ToStringCreator;

@Entity
@Table(name = "owners")
public class Owner extends Person {

  @Column(name = "address")
  @NotEmpty
  private String address;

  @Column(name = "city")
  @NotEmpty
  private String city;

  @Column(name = "telephone")
  @NotEmpty
  @Digits(fraction = 0, integer = 10)
  private String telephone;

  /**
   * 주인은 여러 마리의 동물을 가짐
   * cascadeType:
   * - persist: 저장시 자식도 저장
   * - merge: 병합시 자식도 병합
   * - remove: 삭제시 자식도 삭제
   * - refresh: 새로 고침시 자식도 새로 고침
   * fatchType:
   * - eager: 연관된 엔티티를 즉시 로딩(비추)
   * - lazy: 연관된 엔티티를 지연 로딩(강추)
   */
  @OneToMany(cascade = CascadeType.ALL, mappedBy = "owner", fetch = FetchType.EAGER)
  private Set<Pet> pets;

  public String getAddress() {
    return this.address;
  }

  public void setAddress(String address) {
    this.address = address;
  }

  public String getCity() {
    return this.city;
  }

  public void setCity(String city) {
    this.city = city;
  }

  public String getTelephone() {
    return this.telephone;
  }

  public void setTelephone(String telephone) {
    this.telephone = telephone;
  }

  protected Set<Pet> getPetsInternal() {
    if (this.pets == null) {
      this.pets = new HashSet<>();
    }
    return this.pets;
  }

  protected void setPetsInternal(Set<Pet> pets) {
    this.pets = pets;
  }

  public List<Pet> getPets() {
    List<Pet> sortedPets = new ArrayList<>(getPetsInternal());
    PropertyComparator.sort(sortedPets, new MutableSortDefinition("name", true, true));
    return Collections.unmodifiableList(sortedPets);
  }

  public void setPets(List<Pet> pets) {
    this.pets = new HashSet<>(pets);
  }

  public void addPet(Pet pet) {
    getPetsInternal().add(pet);
    pet.setOwner(this);
  }

  public Pet getPet(String name) {
    return getPet(name, false);
  }

  public Pet getPet(String name, boolean ignoreNew) {
    name = name.toLowerCase();
    for (Pet pet : getPetsInternal()) {
      if (!ignoreNew || !pet.isNew()) {
        String compName = pet.getName();
        compName = compName.toLowerCase();
        if (compName.equals(name)) {
          return pet;
        }
      }
    }
    return null;
  }

  @Override
  public String toString() {
    return new ToStringCreator(this)

        .append("id", this.getId())
        .append("new", this.isNew())
        .append("lastName", this.getLastName())
        .append("firstName", this.getFirstName())
        .append("address", this.address)
        .append("city", this.city)
        .append("telephone", this.telephone)
        .toString();
  }
}
