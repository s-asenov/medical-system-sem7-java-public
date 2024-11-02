package com.medic.system.dtos.doctor;

import com.medic.system.dtos.user.BaseUserRequestDto;
import com.medic.system.enums.Role;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class DoctorRequestDto extends BaseUserRequestDto {
    private Boolean isGeneralPractitioner;

    public DoctorRequestDto() {
        super();
        setRole(Role.ROLE_DOCTOR);
    }
}
