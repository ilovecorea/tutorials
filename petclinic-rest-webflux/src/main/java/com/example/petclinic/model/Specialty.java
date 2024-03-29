package com.example.petclinic.model;

import javax.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.domain.Persistable;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table("specialties")
public class Specialty implements Persistable<Integer> {

  @Id
  protected Integer id;

  @Column("name")
  @NotEmpty
  private String name;

  public boolean isNew() {
    return this.id == null;
  }
}
