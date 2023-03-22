package org.example.petclinic.model;

import java.util.Set;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToMany;
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
@Table(name = "specialties")
public class Specialty extends AbstractPersistable<Integer> {

  @Column(name = "name")
  @NotEmpty
  private String name;

  @ManyToMany(mappedBy = "specialties")
  private Set<Vet> vets;
}
