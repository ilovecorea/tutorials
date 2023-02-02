package com.example.petclinic.model;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.annotation.Transient;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Getter
@Setter
@ToString
@Table("roles")
public class Role extends BaseEntity {

    @Transient
    @ToString.Exclude
    private User user;

    @Column("role")
    private String name;

}
