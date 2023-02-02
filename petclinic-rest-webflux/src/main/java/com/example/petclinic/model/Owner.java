package com.example.petclinic.model;

import java.util.ArrayList;
import java.util.List;
import javax.validation.constraints.Digits;
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
@Table("owners")
public class Owner implements Persistable<Integer> {

  @Id
  protected Integer id;

  @Column("first_name")
  @NotEmpty
  protected String firstName;

  @Column("last_name")
  @NotEmpty
  protected String lastName;

  @Column("address")
  @NotEmpty
  private String address;

  @Column("city")
  @NotEmpty
  private String city;

  @Column("telephone")
  @NotEmpty
  @Digits(fraction = 0, integer = 10)
  private String telephone;

  @Transient
  private List<Pet> pets = new ArrayList<>();

  public boolean isNew() {
    return this.id == null;
  }
}
