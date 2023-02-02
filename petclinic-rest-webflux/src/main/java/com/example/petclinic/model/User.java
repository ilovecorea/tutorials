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
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table("users")
public class User implements Persistable<String> {

    @Id
    @Column("username")
    private String username;

    @Column("password")
    private String password;

    @Column("enabled")
    private Boolean enabled;

    @Transient
    private List<Role> roles;

    @Transient
    private Boolean isNew;

    @Override
    public String getId() {
        return this.username;
    }

    @Override
    public boolean isNew() {
        return this.isNew;
    }
}
