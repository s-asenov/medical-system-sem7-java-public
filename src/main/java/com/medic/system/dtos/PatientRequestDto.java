package com.medic.system.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PatientRequestDto extends BaseUserRequestDto {
    @NotBlank(message = "EGN is mandatory")
    private String egn;

    @NotNull(message = "General practitioner id is mandatory")
    private Long generalPractitionerId;
}
