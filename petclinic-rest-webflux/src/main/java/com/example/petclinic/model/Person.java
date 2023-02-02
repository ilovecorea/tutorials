package com.example.petclinic.model;

import javax.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.relational.core.mapping.Column;

@Getter
@Setter
@ToString
public class Person extends BaseEntity {

  @Column("first_name")
  @NotEmpty
  protected String firstName;

  @Column("last_name")
  @NotEmpty
  protected String lastName;

}
