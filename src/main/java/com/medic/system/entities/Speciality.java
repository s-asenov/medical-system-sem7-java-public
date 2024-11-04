package com.medic.system.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Entity
@Table(name = "specialities")
@Getter
@Setter
public class Speciality extends BaseEntity {
    private String name;

    @ManyToMany(mappedBy = "specialities")
    Set<Doctor> doctors;
}
