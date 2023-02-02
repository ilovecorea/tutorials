package com.example.petclinic.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

/**
 * id 필드만 있는 상위 엔티티
 */
@MappedSuperclass
public class BaseEntity {

  /**
   * identity: 기본 키 생성을 데이타베이스에 위임
   * sequence: 데이타베이스의 sequence object를 사용
   * table: 키 생성 전용 테이블을 만들어서 사용
   * auto: dialect에 따라 자동
   */
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  protected Integer id;

  public Integer getId() {
    return id;
  }

  public void setId(Integer id) {
    this.id = id;
  }

  @JsonIgnore
  public boolean isNew() {
    //id가 null 이면 persist 아니면 merge
    return this.id == null;
  }
}
