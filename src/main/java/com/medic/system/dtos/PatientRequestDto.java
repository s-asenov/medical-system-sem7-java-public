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
public class PatientRequestDto extends BaseUserRequestDto {
    @NotBlank(message = "ЕГН-то е задължително")
    @Unique(entityClass = Patient.class, fieldName = "egn", message = "ЕГН-то вече съществува")
    private String egn;

    @NotNull(message = "Личният лекар е задължителен")
    private Long generalPractitionerId;

    public PatientRequestDto() {
        super();
        this.role = Role.ROLE_PATIENT;
    }
}
