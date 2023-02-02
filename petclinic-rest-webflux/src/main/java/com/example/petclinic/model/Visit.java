package com.example.petclinic.model;

import java.time.LocalDate;
import javax.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Transient;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Getter
@Setter
@ToString
@Table("visits")
public class Visit extends BaseEntity {

    @Column("visit_date")
    @CreatedDate
    private LocalDate date;

    @NotEmpty
    @Column("description")
    private String description;

    @Transient
    @ToString.Exclude
    private Pet pet;

}
