package com.medic.system.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Entity
@Table(name = "medical_appointments")
@Getter
@Setter
public class MedicalAppointment extends BaseEntity {
    @FutureOrPresent(message = "Дата не може да бъде в миналото")
    @Temporal(TemporalType.DATE)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date date;

    @ManyToOne
    @JoinColumn(name = "doctor_id")
    @NotNull(message = "Докторът е задължителен")
    private Doctor doctor;

    @ManyToOne
    @JoinColumn(name = "patient_id")
    @NotNull(message = "Пациентът е задължителен")
    private Patient patient;

    @ManyToOne
    @JoinColumn(name = "diagnose_id")
    @NotNull(message = "Диагнозата е задължителна")
    private Diagnose diagnose;
}
