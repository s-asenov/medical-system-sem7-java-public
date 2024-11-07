package com.medic.system.entities;

import com.medic.system.dtos.doctor.DoctorRequestDto;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "doctors")
@PrimaryKeyJoinColumn(name = "user_id")
@Getter
@Setter
public class Doctor extends User {
    @Column(nullable = false)
    private Boolean isGeneralPractitioner = false;

    @ManyToMany
    @JoinTable(
            name = "doctor_specialities",
            joinColumns = @JoinColumn(name = "doctor_id"),
            inverseJoinColumns = @JoinColumn(name = "speciality_id")
    )
    @OrderBy("name ASC")
    private List<Speciality> specialities = new ArrayList<>();

    public Doctor() {
    }

    public Doctor(DoctorRequestDto doctorRequestDto) {
        super(doctorRequestDto);
        isGeneralPractitioner = doctorRequestDto.getIsGeneralPractitioner();
    }

    public void addSpeciality(Speciality speciality) {
        specialities.add(speciality);
    }

    public void clearSpecialities() {
        specialities.clear();
    }
}
