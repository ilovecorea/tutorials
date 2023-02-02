package com.example.petclinic.model;

import java.util.ArrayList;
import java.util.List;
import javax.validation.constraints.Digits;
import javax.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.annotation.Transient;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Getter
@Setter
@ToString
@Table("owners")
public class Owner extends Person {

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

}
