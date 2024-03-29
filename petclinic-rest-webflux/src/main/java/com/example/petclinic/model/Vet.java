package com.example.petclinic.model;

import java.util.ArrayList;
import java.util.List;
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
@Table("vets")
public class Vet implements Persistable<Integer> {

    @Id
    private Integer id;

    @Column("first_name")
    @NotEmpty
    private String firstName;

    @Column("last_name")
    @NotEmpty
    private String lastName;

    @Transient
    private List<Specialty> specialties = new ArrayList<>();

    public boolean isNew() {
        return this.id == null;
    }
}
