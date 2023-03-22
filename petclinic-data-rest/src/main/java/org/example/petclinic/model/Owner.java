package org.example.petclinic.model;

import java.util.LinkedHashSet;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.Digits;
import javax.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.jpa.domain.AbstractPersistable;

@Getter
@Setter
@ToString
@Entity
@Table(name = "owners")
public class Owner extends AbstractPersistable<Integer> {

  @Column(name = "first_name")
  @NotEmpty
  protected String firstName;

  @Column(name = "last_name")
  @NotEmpty
  protected String lastName;

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

  @ToString.Exclude
  @OneToMany(cascade = CascadeType.ALL, mappedBy = "owner")
  private Set<Pet> pets = new LinkedHashSet<>();
}
