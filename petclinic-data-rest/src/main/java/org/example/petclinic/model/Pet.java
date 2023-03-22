package org.example.petclinic.model;

import java.time.LocalDate;
import java.util.LinkedHashSet;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
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
@Table(name = "pets")
public class Pet extends AbstractPersistable<Integer> {

  @Column(name = "name")
  @NotEmpty
  private String name;

  @Column(name = "birth_date", columnDefinition = "DATE")
  private LocalDate birthDate;

  @ManyToOne
  @JoinColumn(name = "type_id")
  private PetType type;

  @ManyToOne
  @JoinColumn(name = "owner_id")
  private Owner owner;

  @ToString.Exclude
  @OneToMany(cascade = CascadeType.ALL, mappedBy = "pet", fetch = FetchType.LAZY)
  private Set<Visit> visits = new LinkedHashSet<>();

}
