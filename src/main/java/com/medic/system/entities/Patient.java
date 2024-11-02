package com.medic.system.entities;

import com.medic.system.dtos.PatientRequestDto;
import com.medic.system.enums.Role;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import javax.print.Doc;

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
        this.setRole(Role.ROLE_PATIENT);
    }

    public Patient(PatientRequestDto patientRequestDto, Doctor doctor) {
        this();
        this.setEgn(patientRequestDto.getEgn());
        this.setFirstName(patientRequestDto.getFirstName());
        this.setLastName(patientRequestDto.getLastName());
        this.setUsername(patientRequestDto.getUsername());
        this.setPassword(patientRequestDto.getPassword());
        this.setGeneralPractitioner(doctor);
    }
}
