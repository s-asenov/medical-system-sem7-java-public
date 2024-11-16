package com.medic.system.dtos.sick_leave;

import com.medic.system.entities.SickLeave;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Getter
@Setter
public class EditSickLeaveRequestDto {
    private Long id;

    @NotNull(message = "Прегледът е задължителен")
    private Long medicalAppointmentId;

    @NotNull(message = "Дата на започване е задължителна")
    @FutureOrPresent(message = "Дата на започване не може да бъде в миналото")
    @Temporal(TemporalType.DATE)
    @DateTimeFormat(pattern="yyyy-MM-dd")
    private LocalDate startDate;

    @NotNull
    @Min(value = 1, message = "Броят на дните трябва да бъде поне 1")
    private Integer days;

    public EditSickLeaveRequestDto() {
    }

    public EditSickLeaveRequestDto(SickLeave sickLeave) {
        id = sickLeave.getId();
        medicalAppointmentId = sickLeave.getMedicalAppointment().getId();
        startDate = sickLeave.getStartDate();
        days = sickLeave.getDays();
    }
}