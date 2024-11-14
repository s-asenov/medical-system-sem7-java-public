package com.medic.system.dtos.patient;

import com.medic.system.dtos.user.BaseUserSearchDto;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PatientSearchDto extends BaseUserSearchDto {
    private Long generalPractitionerId;
}
