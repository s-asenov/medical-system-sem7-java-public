package com.medic.system.dtos.medical_appointment;

import com.medic.system.dtos.user.BaseUserSearchDto;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Getter
@Setter
public class MedicalAppointmentSearchDto extends BaseUserSearchDto {
    private Long diagnoseId;

    private Long doctorId;

    private Long patientId;

    @Temporal(TemporalType.DATE)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate startDate;

    @Temporal(TemporalType.DATE)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate endDate;
}
