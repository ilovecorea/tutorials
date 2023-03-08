package com.example.petclinic.model;

import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.domain.Persistable;
import org.springframework.data.relational.core.mapping.Table;

@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table("owners")
public class Owner implements Persistable<Integer> {

  @Id
  private Integer id;

  private String firstName;

  private String lastName;

  private String address;

  private String city;

  private String telephone;

  @Transient
  private List<Pet> pets = new ArrayList<>();

  public boolean isNew() {
    return this.id == null;
  }

}
