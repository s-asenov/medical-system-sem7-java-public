package com.medic.system.dtos.medical_appointment;

import com.medic.system.dtos.user.BaseUserSearchDto;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MedicalAppointmentSearchDto extends BaseUserSearchDto {
    private Long diagnoseId;
}
