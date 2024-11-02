package com.medic.system.entities;

import com.medic.system.dtos.patient.PatientRequestDto;
import com.medic.system.enums.Role;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "patients")
@PrimaryKeyJoinColumn(name = "user_id")
@Getter
@Setter
public class Patient extends User {

    @Column(unique = true, nullable = false)
    private String egn;

    @ManyToOne
    @JoinColumn(name = "general_practitioner_id")
    private Doctor generalPractitioner;

    public Patient() {
        super();
        setRole(Role.ROLE_PATIENT);
    }

    public Patient(PatientRequestDto patientRequestDto, Doctor doctor) {
        this();
        setEgn(patientRequestDto.getEgn());
        setFirstName(patientRequestDto.getFirstName());
        setLastName(patientRequestDto.getLastName());
        setUsername(patientRequestDto.getUsername());
        setPassword(patientRequestDto.getPassword());
        setGeneralPractitioner(doctor);
    }
}
