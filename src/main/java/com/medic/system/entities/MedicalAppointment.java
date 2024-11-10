package com.medic.system.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Entity
@Table(name = "medical_appointments")
@Getter
@Setter
public class MedicalAppointment extends BaseEntity {
    @FutureOrPresent(message = "Дата не може да бъде в миналото")
    @Temporal(TemporalType.DATE)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @NotNull
    private LocalDate date;

    @ManyToOne
    @JoinColumn(name = "doctor_id", nullable = false)
    @NotNull(message = "Докторът е задължителен")
    private Doctor doctor;

    @ManyToOne
    @JoinColumn(name = "patient_id", nullable = false)
    @NotNull(message = "Пациентът е задължителен")
    private Patient patient;

    @ManyToOne
    @JoinColumn(name = "diagnose_id", nullable = false)
    @NotNull(message = "Диагнозата е задължителна")
    private Diagnose diagnose;

    @OneToOne(mappedBy = "medicalAppointment")
    private SickLeave sickLeave;
}
