package com.medic.system.dtos;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PatientRequestDto {
    @NotBlank(message = "EGN is mandatory")
    private String egn;

    @NotBlank(message = "General practitioner id is mandatory")
    private Long generalPractitionerId;
}
