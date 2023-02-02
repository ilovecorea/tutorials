package com.example.petclinic.model;

import java.util.ArrayList;
import java.util.List;
import org.springframework.data.annotation.Transient;
import org.springframework.data.relational.core.mapping.Table;

@Table("vets")
public class Vet extends Person {

    @Transient
    private List<Specialty> specialties = new ArrayList<>();

    public void addSpecialty(Specialty specialty) {
        specialties.add(specialty);
    }

}
