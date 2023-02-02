package com.example.petclinic.model;

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
@Table("roles")
public class Role implements Persistable<Integer> {

    @Id
    protected Integer id;

    @Transient
    @ToString.Exclude
    private User user;

    @Column("role")
    private String name;

    public boolean isNew() {
        return this.id == null;
    }
}
