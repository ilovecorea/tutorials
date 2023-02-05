package com.example.petclinic.model;

import java.time.LocalDate;
import javax.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.annotation.CreatedDate;
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
@Table("visits")
public class Visit implements Persistable<Integer> {

    @Id
    private Integer id;

    @Column("pet_id")
    private Integer petId;

    @Column("visit_date")
    @CreatedDate
    private LocalDate date;

    @NotEmpty
    @Column("description")
    private String description;

    @Transient
    @ToString.Exclude
    private Pet pet;

    public boolean isNew() {
        return this.id == null;
    }
}
