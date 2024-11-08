package com.medic.system.entities;

import com.medic.system.dtos.patient.PatientRequestDto;
import com.medic.system.enums.Role;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "patients")
@PrimaryKeyJoinColumn(name = "user_id")
@Getter
@Setter
public class Patient extends User {

    @Column(unique = true, nullable = false)
    @NotBlank(message = "ЕГН-то е задължително")
    private String egn;

    @ManyToOne
    @JoinColumn(name = "general_practitioner_id")
    private Doctor generalPractitioner;

    @OneToMany(mappedBy = "patient")
    private List<MedicalAppointment> medicalAppointments = new ArrayList<>();

    public Patient() {
        super();
        setRole(Role.ROLE_PATIENT);
    }

    public Patient(PatientRequestDto patientRequestDto, Doctor doctor) {
        super(patientRequestDto);
        setRole(Role.ROLE_PATIENT);
        setEgn(patientRequestDto.getEgn());
        setGeneralPractitioner(doctor);
    }
}
