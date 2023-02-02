package com.example.petclinic.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.domain.Persistable;

/**
 * id 필드만 있는 상위 엔티티
 */
@Data
public class BaseEntity implements Persistable<Integer> {

  @Id
  protected Integer id;

  @JsonIgnore
  public boolean isNew() {
    //id가 null 이면 persist 아니면 merge
    return this.id == null;
  }
}
