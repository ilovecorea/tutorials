package org.example.petclinic.model;

import java.util.LinkedHashSet;
import java.util.Set;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import javax.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.jpa.domain.AbstractPersistable;

@Getter
@Setter
@ToString
@Entity
@Table(name = "vets")
public class Vet extends AbstractPersistable<Integer> {

  @Column(name = "first_name")
  @NotEmpty
  private String firstName;

  @Column(name = "last_name")
  @NotEmpty
  private String lastName;

  @ToString.Exclude
  @OrderBy("name asc ")
  @ManyToMany(fetch = FetchType.EAGER)
  @JoinTable(name = "vet_specialties", joinColumns = @JoinColumn(name = "vet_id"),
      inverseJoinColumns = @JoinColumn(name = "specialty_id"))
  private Set<Specialty> specialties = new LinkedHashSet<>();
}
