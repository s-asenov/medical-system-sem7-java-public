package com.medic.system.dtos.medical_appointment;

import com.medic.system.entities.MedicalAppointment;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

@Getter
@Setter
public class EditMedicalAppointmentRequestDto {
    private Long id;

    @NotNull(message = "Докторът е задължителен")
    private Long doctorId;

    @NotNull(message = "Пациентът е задължителен")
    private Long patientId;

    @NotNull(message = "Диагнозата е задължителна")
    private Long diagnoseId;

    @NotNull(message = "Дата е задължителна")
    @FutureOrPresent(message = "Дата не може да бъде в миналото")
    @Temporal(TemporalType.DATE)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate date;

    private List<Long> drugs;

    public EditMedicalAppointmentRequestDto() {
    }

    public EditMedicalAppointmentRequestDto(MedicalAppointment appointment) {
        id = appointment.getId();
        doctorId = appointment.getDoctor().getId();
        patientId = appointment.getPatient().getId();
        diagnoseId = appointment.getDiagnose().getId();
        date = appointment.getDate();
        drugs = appointment.getDrugs().stream().map(drug -> drug.getId()).toList();
    }
}