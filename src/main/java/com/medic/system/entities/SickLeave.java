package com.medic.system.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Entity
@Table(name = "sick_leaves")
@Getter
@Setter
public class SickLeave extends BaseEntity {
    @OneToOne
    @JoinColumn(name = "medical_appointment_id", nullable = false)
    @NotNull
    private MedicalAppointment medicalAppointment;

    @FutureOrPresent(message = "Дата на започване не може да бъде в миналото")
    @Temporal(TemporalType.DATE)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate startDate;

    @NotNull
    @Min(value = 1, message = "Броят на дните трябва да бъде поне 1")
    private Integer days;
}
