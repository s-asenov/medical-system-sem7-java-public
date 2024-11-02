package com.medic.system.dtos;

import com.medic.system.annotations.Unique;
import com.medic.system.entities.Patient;
import com.medic.system.enums.Role;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Unique(entityClass = Patient.class, fieldName = "egn", message = "ЕГН-то вече съществува")
public class PatientRequestDto extends BaseUserRequestDto {
    @NotBlank(message = "ЕГН-то е задължително")
    private String egn;

    @NotNull(message = "Личният лекар е задължителен")
    private Long generalPractitionerId;

    public PatientRequestDto() {
        super();
        this.setRole(Role.ROLE_PATIENT);
    }
}
