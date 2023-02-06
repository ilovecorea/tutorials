package com.example.petclinic.model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import javax.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.domain.Persistable;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table("pets")
public class Pet implements Persistable<Integer> {

  @Id
  private Integer id;

  @Column("name")
  @NotEmpty
  private String name;

  @Column("birth_date")
  private LocalDate birthDate;

  @NotEmpty
  @Column("type_id")
  private Integer typeId;

  @NotEmpty
  @Column("owner_id")
  private Integer ownerId;

  @Transient
  private PetType type;

  @Transient
  @ToString.Exclude
  private Owner owner;

  @Transient
  private List<Visit> visits;

  public Pet setType(PetType type) {
    this.type = type;
    this.setTypeId(type.getId());
    return this;
  }

  public void setOwner(Owner owner) {
    this.owner = owner;
    this.setOwnerId(owner.getId());
  }

  public boolean isNew() {
    return this.id == null;
  }

  public void addVisit(Visit visit) {
    if (visits == null) {
      visits = new ArrayList<>();
    }
    visits.add(visit);
  }
}
