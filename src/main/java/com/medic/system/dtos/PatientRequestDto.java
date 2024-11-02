package com.medic.system.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PatientRequestDto extends BaseUserRequestDto {
    @NotBlank(message = "ЕГН-то е задължително")
    private String egn;

    @NotNull(message = "Личният лекар е задължителен")
    private Long generalPractitionerId;
}
