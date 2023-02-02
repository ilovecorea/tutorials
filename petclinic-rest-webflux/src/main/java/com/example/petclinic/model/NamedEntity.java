package com.example.petclinic.model;

import javax.validation.constraints.NotEmpty;
import lombok.Data;
import org.springframework.data.relational.core.mapping.Column;

/**
 * id, name 필드만 있는 상위 엔티티
 */
@Data
public class NamedEntity extends BaseEntity {

  @Column("name")
  @NotEmpty
  private String name;

}
