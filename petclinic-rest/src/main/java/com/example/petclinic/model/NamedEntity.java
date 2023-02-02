package com.example.petclinic.model;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import javax.validation.constraints.NotEmpty;

/**
 * id, name 필드만 있는 상위 엔티티
 */
@MappedSuperclass
public class NamedEntity extends BaseEntity {

  @Column(name = "name")
  @NotEmpty
  private String name;

  public String getName() {
    return this.name;
  }

  public void setName(String name) {
    this.name = name;
  }

  @Override
  public String toString() {
    return this.getName();
  }
}
