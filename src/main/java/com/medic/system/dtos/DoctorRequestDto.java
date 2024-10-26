package com.medic.system.dtos;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class DoctorRequestDto extends BaseUserRequestDto{
    public boolean isGeneralPractitioner;

    @NotNull(message = "Specialties are mandatory")
    private List<String> specialties;
}
