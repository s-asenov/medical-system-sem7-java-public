package com.medic.system.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "medical_exams")
@Getter
@Setter
public class MedicalExam extends BaseEntity {
    @ManyToOne
    @JoinColumn(name = "doctor_id")
    private Doctor doctor;

    @ManyToOne
    @JoinColumn(name = "patient_id")
    private Patient patient;

    private LocalDate date;

    @OneToMany(mappedBy = "medicalExam")
    private Set<Diagnose> diagnoses = new HashSet<>();
}
